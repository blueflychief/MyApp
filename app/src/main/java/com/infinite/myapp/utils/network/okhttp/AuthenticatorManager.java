package com.infinite.myapp.utils.network.okhttp;

import android.text.TextUtils;

import com.infinite.myapp.utils.network.auth.AuthManager;
import com.infinite.myapp.utils.network.auth.AuthUtility;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class AuthenticatorManager implements Authenticator {
    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        System.out.println("Authenticating for response: " + response);
        System.out.println("Challenges: " + response.challenges());
        if (responseCount(response) >= 3) {
            return null; // If we've failed 3 times, give up.
        }

        String userName = null;
        String passWord = null;

        if (AuthManager.getInstance().isExplicitLogin()) {
            userName = AuthManager.getInstance().getTempUserName();
            passWord = AuthManager.getInstance().getTempPassword();
        } else {
            AuthManager authManager = AuthManager.getInstance();
            userName = authManager.getLoginUser(false).mUID;
            passWord = authManager.getPswByUID("token.http.base", userName, false);
        }

        if (TextUtils.isEmpty(userName)) {
            return null;
        }

        String www_Authenticate = response.headers().get("WWW-Authenticate");
        String toMd5 = (www_Authenticate + userName + AuthUtility
                .base64Encode(AuthUtility.md5Digest32(passWord).getBytes())).replace("\n", "");

        String authString = AuthUtility.base64Encode(AuthUtility
                .md5Digest32(toMd5).getBytes());
        authString = authString.replace("\n", "");
        String authenticate = "user=\"" + userName + "\",response=\"" + authString + "\"";

        return response.request().newBuilder()
                .header("Authorization", authenticate)
                .build();
    }

    /**
     * 重复请求次数限制
     *
     * @param response
     * @return
     */
    private int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }
}
