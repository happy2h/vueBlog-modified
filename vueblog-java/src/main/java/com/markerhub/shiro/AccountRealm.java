package com.markerhub.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.markerhub.entity.User;
import com.markerhub.service.UserService;
import com.markerhub.util.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 授权
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 认证
        JwtToken jwtToken = (JwtToken) token; // 获取jwtToken

        // 通过jwtToken获取用户用户信息，一般用subject来存用户id
        String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();

        // 获取数据库中用户信息
        User user = userService.getById(Long.valueOf(userId));
        if (user == null) {
            throw new UnknownAccountException("账户不存在");
        }

        if (user.getStatus() == -1) {
            throw new LockedAccountException("账户已被锁定");
        }

        // 校验通过
        // 封装成SimpleAuthenticationInfo返回给shiro
        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(user, profile);

        return new SimpleAuthenticationInfo(profile, jwtToken.getCredentials(), getName());
    }
}
