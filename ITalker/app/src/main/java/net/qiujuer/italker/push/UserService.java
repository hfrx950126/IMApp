package net.qiujuer.italker.push;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class UserService implements IUserService {
    @Override
    public String search(int hashCode) {
        return "User:" + hashCode;
    }
}
