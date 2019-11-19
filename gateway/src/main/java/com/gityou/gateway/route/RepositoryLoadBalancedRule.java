package com.gityou.gateway.route;

import com.gityou.gateway.service.ZuulService;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/*
 * 负载
 * */
public class RepositoryLoadBalancedRule extends AbstractLoadBalancerRule {
    @Autowired
    private ZuulService zuulService;

    private RoundRobinRule roundRobinRule = new RoundRobinRule();

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }

    @Override
    public Server choose(Object key) {
        ILoadBalancer lb = getLoadBalancer();
        RequestContext requestContext = RequestContext.getCurrentContext();

        String uri = (String) requestContext.get("requestURI");
        if (uri.equals("/file/list")) {
            String username = requestContext.getRequest().getParameter("user");
            String repositoryName = requestContext.getRequest().getParameter("name");

            List<Integer> machines = zuulService.getMachine(username, repositoryName);

            List<Server> allServers = lb.getAllServers();
            List<Server> reallyServers = new ArrayList<>(machines.size());

            // 查找所在的实例
            for (Server server : allServers) {
                DiscoveryEnabledServer discoveryEnabledServer = (DiscoveryEnabledServer) server;
                Integer machineId = Integer.valueOf(discoveryEnabledServer.getInstanceInfo().getMetadata().get("machineId"));

                for (Integer e : machines)
                    if (e.equals(machineId)) {
                        reallyServers.add(server);
                        machines.remove(e);
                        break;
                    }
                if (machines.size() == 0) break;
            }

            // 随机原则
            int cur = (int) (Math.random() * reallyServers.size());
            return reallyServers.get(cur);
        }


        /* 普通轮询 */
        if (roundRobinRule != null) {
            return roundRobinRule.choose(key);
        } else {
            throw new IllegalArgumentException(
                    "This class has not been initialized with the RoundRobinRule class");
        }
    }


    @Override
    public void setLoadBalancer(ILoadBalancer lb) {
        super.setLoadBalancer(lb);
        roundRobinRule.setLoadBalancer(lb);
    }
}
