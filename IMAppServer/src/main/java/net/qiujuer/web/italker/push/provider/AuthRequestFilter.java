package net.qiujuer.web.italker.push.provider;

import com.google.common.base.Strings;
import net.qiujuer.web.italker.push.bean.api.base.ResponseModel;
import net.qiujuer.web.italker.push.bean.db.User;
import net.qiujuer.web.italker.push.factory.UserFactory;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

/**
 * 用于所有的请求的接口的过滤和拦截ø
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
@Provider
public class AuthRequestFilter implements ContainerRequestFilter {

    // 实现接口的过滤方法
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // 检查是否是登录注册接口
        String relationPath = ((ContainerRequest) requestContext).getPath(false);
        if (relationPath.startsWith("account/login")
                || relationPath.startsWith("account/register")) {
            // 直接走正常逻辑，不做拦截
            return;
        }


        // 从Headers中去找到第一个token节点
        String token = requestContext.getHeaders().getFirst("token");
        if (!Strings.isNullOrEmpty(token)) {

            // 查询自己的信息
            final User self = UserFactory.findByToken(token);
            if (self != null) {
                // 给当前请求添加一个上下文
                requestContext.setSecurityContext(new SecurityContext() {
                    // 主体部分
                    @Override
                    public Principal getUserPrincipal() {
                        // User 实现 Principal接口
                        return self;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        // 可以在这里写入用户的权限，role 是权限名，
                        // 可以管理管理员权限等等
                        return true;
                    }

                    @Override
                    public boolean isSecure() {
                        // 默认false即可，HTTPS
                        return false;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        // 不用理会
                        return null;
                    }
                });
                // 写入上下文后就返回
                return;
            }
        }

        // 直接返回一个账户需要登录的Model
        ResponseModel model = ResponseModel.buildAccountError();
        // 构建一个返回
        Response response = Response.status(Response.Status.OK)
                .entity(model)
                .build();
        // 拦截，停止一个请求的继续下发，调用该方法后之间返回请求
        // 不会走到Service中去
        requestContext.abortWith(response);

    }
}
