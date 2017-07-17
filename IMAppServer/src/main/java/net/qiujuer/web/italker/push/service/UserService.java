package net.qiujuer.web.italker.push.service;

import com.google.common.base.Strings;
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
        if(self.getId().equalsIgnoreCase(followId)||Strings.isNullOrEmpty(followId)){
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
    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //获取某人的信息
    public ResponseModel<UserCard> getUser(@PathParam("id") String id){
        if(Strings.isNullOrEmpty(id)){
            //返回参数异常
            return ResponseModel.buildParameterError();
        }
        User self = getSelf();
        if(getSelf().getId().equalsIgnoreCase(id)){
            //返回自己，不必查询数据库
            return ResponseModel.buildOk(new UserCard(self,true));
        }
        User user = UserFactory.findById(id);
        if(user == null ){
            //没找到用户
            return ResponseModel.buildNotFoundUserError(null);
        }
        //如果我们之间有关注的记录，则我已关注需要查询的用户
        boolean isFollow = UserFactory.getUserFollow(self,user)!=null;
        return ResponseModel.buildOk(new UserCard(user,isFollow));

    }

    //搜索人的接口实现
    //为了简化分页，只返回20条数据
    @GET  //搜索人，不涉及数据更改，只是查询，用GET
    @Path("/search/{name:(.*)?}") //名字为任意字符，可以为空
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> search(@DefaultValue("")@PathParam("name") String name){
        User self = getSelf();

        //先查询数据
        List<User> searchUsers = UserFactory.search(name);
        //把查询的人封装为UserCard
        //判断这些人是否有问我已关注的人，
        //如果有，则返回的关注状态中应该已经设置好状态

        //拿出我的联系人
        List<User> contacts = UserFactory.contacts(self);

        List<UserCard> userCards = searchUsers.stream()
                .map(user -> {
                    //判断这个人是否在我的联系人中。
                    boolean isFollow = user.getId().equalsIgnoreCase(self.getId())
                            //进行联系人的任意匹配，或者是我的怎人联系人当周的人
                            ||contacts.stream().anyMatch(contactUser->contactUser.getId()
                    .equalsIgnoreCase(user.getId()));
                    return new UserCard(user,isFollow);
                }).collect(Collectors.toList());

        return ResponseModel.buildOk(userCards);
    }


}
