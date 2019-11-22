package com.gityou.git.resolver;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.ServiceMayNotContinueException;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

public class GitHttpResolver implements RepositoryResolver<HttpServletRequest> {
    @Override
    public Repository open(HttpServletRequest request, String name) throws RepositoryNotFoundException, ServiceNotAuthorizedException, ServiceNotEnabledException, ServiceMayNotContinueException {
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        System.out.println(contextPath);
        System.out.println(servletPath);

        try (Repository repo = Git.open(new File("D:\\tmp\\gityou\\repository\\xiaorui\\url.git")).getRepository()) {
            return repo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
