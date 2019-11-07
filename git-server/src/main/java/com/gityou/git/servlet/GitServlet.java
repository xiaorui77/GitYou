package com.gityou.git.servlet;


import com.gityou.git.resolver.GitHttpResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;


@WebServlet(name = "gitServlet", urlPatterns = {"/*"},
        loadOnStartup = 1, initParams = {
        @WebInitParam(name = "base-path", value = "D:\\tmp\\gityou\\repository\\"),
        @WebInitParam(name = "export-all", value = "true")
})
public class GitServlet extends org.eclipse.jgit.http.server.GitServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        setRepositoryResolver(new GitHttpResolver());
    }
}
