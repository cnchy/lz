package com.iotlab.integrityarchives.realm;

import com.iotlab.integrityarchives.entity.UserToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ModularRealmAuthenticator类主要是用来实现项目中出现多realm的情况下，根据认证策略来对各个realm进行认证，它继承了AbstractAuthenticator抽象类，先对其解析如下
 */
public class UserModularRealmAuthenticator extends ModularRealmAuthenticator {

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        System.out.println("UserModularRealmAuthenticator:method doAuthenticate() execute ");
        // 判断getRealms()是否返回为空
        assertRealmsConfigured();
        // 强制转换回自定义的CustomizedToken
        UserToken userToken = (UserToken) authenticationToken;
        // 登录类型
        String loginType = userToken.getLoginType();
        // 所有Realm
        Collection<Realm> realms = getRealms();
        // 登录类型对应的所有Realm
        List<Realm> typeRealms = new ArrayList<>();
        for (Realm realm : realms) {
            if (realm.getName().contains(loginType)) {
                System.out.println(realm.getName());
                System.out.println(loginType);
                typeRealms.add(realm);
            }
        }

        // 判断是单Realm还是多Realm
        if (typeRealms.size() == 1){
            System.out.println("doSingleRealmAuthentication() execute ");
            return doSingleRealmAuthentication(typeRealms.get(0), userToken);
        }
        else{
            System.out.println("doMultiRealmAuthentication() execute ");
            return doMultiRealmAuthentication(typeRealms, userToken);
        }
    }
}
