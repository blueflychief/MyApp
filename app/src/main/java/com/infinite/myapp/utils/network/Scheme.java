package com.infinite.myapp.utils.network;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;


public class Scheme {

    private Class mClass;

    private Object mObject;

    public Scheme(String moduleName) {
        findClass(moduleName);
    }

    private Set<String> mTagSet;

    private void findClass(String moduleName) {
        try {
            mClass = Class.forName("com.kk.buz.service." + moduleName + "Service");
            mTagSet = new HashSet<>();
            mObject = mClass.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void get(String methodName, Object... params) {
        try {
            Method method = getMethod(methodName, params);

            if (method == null) {
                throw new IllegalStateException("no match method " + methodName);
            }

            mTagSet.add(params[0].toString());
            method.invoke(mObject, params);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Method getMethod(String methodName, Object... params) {
        for (Method method : mClass.getMethods()) {
            if (method.getName().equals(methodName)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (params.length != parameterTypes.length) {
                    continue;
                }
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (!isParameterMatch(params[i].getClass(), parameterTypes[i])) {
                        return null;
                    }
                }
                return method;
            }
        }
        return null;
    }

    private boolean isParameterMatch(Class UIParam, Class methodParam) {
        if (UIParam == methodParam) {
            return true;
        } else {
            Class[] interfaces = UIParam.getInterfaces();
            if (interfaces != null) {
                for (Class inter :
                        interfaces) {
                    if (inter == methodParam) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void cancelSchemeNetWork() {
        if (mTagSet.isEmpty()) {
            return;
        }
        for (String tag : mTagSet) {
            KKNetWorkRequest.getInstance().removeTagCall(tag);
            mTagSet.remove(tag);
        }
    }
}
