package net.qiujuer.web.italker.push.service;

import net.qiujuer.web.italker.push.bean.api.base.ResponseModel;
import net.qiujuer.web.italker.push.bean.api.user.UpdateInfoModel;
import net.qiujuer.web.italker.push.bean.card.UserCard;
import net.qiujuer.web.italker.push.bean.db.User;
import net.qiujuer.web.italker.push.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息处理的Service
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
// 127.0.0.1/api/user/...
@Path("/user")
public class UserService extends BaseService {

    // 用户信息修改接口
    // 返回自己的个人信息
    @PUT
    //@Path("") //127.0.0.1/api/user 不需要写，就是当前目录
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> update(UpdateInfoModel model) {
        if (!UpdateInfoModel.check(model)) {
            return ResponseModel.buildParameterError();
        }

        User self = getSelf();
        // 更新用户信息
        self = model.updateToUser(self);
        self = UserFactory.update(self);
        // 构架自己的用户信息
        UserCard card = new UserCard(self, true);
        // 返回
        return ResponseModel.buildOk(card);
    }
    //拉取联系人
    @GET
    @Path("/contact") //127.0.0.1/api/user 不需要写，就是当前目录
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> contact(){
        User self = getSelf();
        //拿到我的联系人
        List<User> users = UserFactory.contacts(self);
        //转换为UserCard
        List<UserCard> userCards =  users.stream()
                //map操作，相当于转置操作，User->UserCard
                .map(user -> {
                    return new UserCard(user,true);
                }).collect(Collectors.toList());
        //返回
        return ResponseModel.buildOk(userCards);
    }
    //关注人
    //关注人的操作其实是双方同时关注的操作
    @PUT   //修改类使用Put
    @Path("/follow/{followId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> follow(@PathParam("followId") String followId){
        User self = getSelf();

        //不能关注我自己
        if(self.getId().equalsIgnoreCase(followId)){
            //返回参数异常
            return ResponseModel.buildParameterError();
        }
        //找到我要关注的人
        User followUser = UserFactory.findById(followId);
        if(followUser == null){
            //未找到人
            return ResponseModel.buildNotFoundUserError(null);
        }

        //备注默认没有，后面可以扩展。
        followUser = UserFactory.follow(self,followUser,null);
        if(followUser==null){
            //关注失败，返回服务器一场异常
            return ResponseModel.buildServiceError();
        }
        //TODO 通知我关注的人我关注他

        //返回关注的人的信息
        return ResponseModel.buildOk(new UserCard(followUser,true));
    }
    //获取某人的信息
    public ResponseModel<UserCard> getUser(String id){

    }
}
