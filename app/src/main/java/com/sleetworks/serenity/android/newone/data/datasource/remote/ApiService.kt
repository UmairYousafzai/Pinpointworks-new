package com.sleetworks.serenity.android.newone.data.datasource.remote

import com.sleetworks.serenity.android.newone.data.models.remote.response.ApiResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.LoginResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.User
import com.sleetworks.serenity.android.newone.data.models.remote.response.auth.UserResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.Workspace
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.WorkspaceResponse
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.share.Share
import com.sleetworks.serenity.android.newone.data.models.remote.response.workspace.site.Site
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    /////////// AUTH //////////////
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("passwordHash") passwordHash: String
    ): Response<ApiResponse<Any>>

    @Headers("Content-Type: application/json")
    @GET("auth/user-exists/{userEmail}")
    suspend fun checkIfUserExists(@Path("userEmail") email: String): Response<Unit>

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun loginWithCode(
        @Field("email") email: String,
        @Field("passwordHash") passwordHash: String,
        @Field("twoFactorAuthCode") twoFactorAuthCode: String
    ): Response<ApiResponse<LoginResponse>>


    @GET("auth/logged")
    suspend fun getLogged(
        @Query("token") token: String,
        @Query("email") email: String
    ): Response<ApiResponse<UserResponse>>


    /////////// Sit //////////////

    @GET("site")
    suspend fun getAllSites(): Response<ApiResponse<List<Site>>>

    /////////// Share //////////////
    @GET("share")
    suspend fun getAllShares(): Response<ApiResponse<List<Share>>>

    /////////// Workspace //////////////
    @GET("account/allworkspaces?showHidden=false")
    suspend fun getAllWorkspaces(): Response<ApiResponse<List<WorkspaceResponse>>>


//    /////////// Defects //////////////
//    @GET("points")
//    fun getDefects(
//        @Query("lastSyncTime") lastSyncTime: String,
//        @Query("workspaceId") spaceId: String
//    ): Call<ApiResponse<PointResult>>
//
//    @POST("points")
//    fun addDefects(
//        @Body body: DefectForSending,
//        @Query("workspaceId") spaceId: String
//    ): Call<ApiResponse<DefectType>>
//
//    @GET("images/fetch-all-images/%1\$s")
//    fun getImagesForPoint(): Call<ApiResponse<Map<String, String>>>
//
//    @GET("account/allworkspaces?showHidden=false")
//    fun listAllWorkspaces(): Call<ApiResponse<List<WorkspaceWrapper>>>
//
//    @GET("share")
//    fun getAllShares(): Call<ApiResponse<List<Share>>>
//
//    @GET("points/{id}")
//    fun getDefectsDetail(
//        @Path("id") pointId: String,
//        @Query("workspaceId") spaceId: String
//    ): Call<ApiResponse<DefectType>>
//
//    /* *********************************** Sites ******************/
//    @GET("site")
//    fun getAllSites(): Call<ApiResponse<List<Site>>>
//
//    @GET("site/{siteId}")
//    fun getSite(@Path("siteId") siteId: String): Call<ApiResponse<Site>>
//
//    @POST("site")
//    fun createSite(@Body site: String): Call<ApiResponse<Site>>
//
//    @PUT("site/{accountId}")
//    fun updateSite(
//        @Path("accountId") accountId: String,
//        @Body site: String
//    ): Call<ApiResponse<Site>>
//
//    @GET("tiles/workspaces/{workspaceId}/first-four-layers")
//    fun getFourSites(@Path("workspaceId") workspaceId: String): Call<ApiResponse<Map<String, String>>>
//
//    @GET("tiles/workspaces/{workspaceId}/remaining-layers")
//    fun getRemainingLayers(@Path("workspaceId") workspaceId: String): Call<ApiResponse<Map<String, String>>>
//
//    /* *********************************** Comments ******************/
//    @POST("comments/points/{id}")
//    fun addComment(
//        @Path("id") defectId: String,
//        @Body comment: DefectComment
//    ): Call<ApiResponse<DefectComment>>
//
//    @GET("comments/points/{id}")
//    fun getCommentPoint(@Path("id") pointId: String): Call<ApiResponse<List<DefectComment>>>
//
//    @GET("comments/workspaces/{id}")
//    fun getComments(
//        @Path("id") workspaceId: String,
//        @Query("lastSyncTime") lastSyncTime: String
//    ): Call<ApiResponse<CommentResult>>
//
//    /* *********************************** Points Reaction ******************/
//    @GET("reactions/points/{pointId}")
//    fun getPointReactions(@Path("pointId") pointId: String): Call<ApiResponse<List<ReactionEntity>>>
//
//    @POST("reactions/comments/{defectId}/like")
//    fun addReaction(
//        @Path("defectId") defectId: String,
//        @Query("remove") remove: Boolean
//    ): Call<ApiResponse<ReactionEntity>>
//
//    /* *********************************** Images ******************/
//    @GET("images/{id}")
//    fun getImage(@Path("id") imageId: String): Call<ApiResponse<Image>>
//
//    @GET("images/fetch-all-images/{id}")
//    fun getImagesForPoint(@Path("id") pointId: String): Call<ApiResponse<Map<String, String>>>
//
//    @GET("images/itemref/{id}")
//    fun getImageListForPoint(@Path("id") pointId: String): Call<ApiResponse<List<Image>>>
//
//    @PUT("points/{id}/simple-update?forceCorrectFieldsUpdate=true")
//    fun updatePointFields(
//        @Path("id") pointId: String,
//        @Body params: RequestBody
//    ): Call<ApiResponse<UpdatedPoint>>
//
//    @PUT("points/{id}/simple-update?forceCorrectFieldsUpdate=true")
//    fun updatePointCustomField(
//        @Path("id") pointId: String,
//        @Body params: DefectForSending
//    ): Call<ApiResponse<UpdatedPoint>>
//
//    @DELETE("images/{id}")
//    fun removeImage(
//        @Path("id") imageId: String,
//        @Query("pointId") pointId: String
//    ): Call<ApiResponse<String>>
//
//    @DELETE("video/{id}")
//    fun removeVideo(
//        @Path("id") id: String,
//        @Query("pointId") pointId: String
//    ): Call<ApiResponse<String>>
//
//    @Multipart
//    @POST("images/{workspaceId}")
//    fun uploadImage(
//        @Part("exif") exif: RequestBody,
//        @Part("itemRefId") itemRefId: RequestBody,
//        @Part("itemRefType") itemRefType: RequestBody,
//        @Part("itemRefCaption") itemRefCaption: RequestBody,
//        @Path("workspaceId") workspaceId: String,
//        @Part imageFile: MultipartBody.Part,
//        @Query("updatePoint") isQuick: Boolean
//    ): Call<ApiResponse<List<String>>>
//
//    @Multipart
//    @POST("images/{workspaceId}?updatePoint=true")
//    fun uploadImageFileQuick(
//        @Part("exif") exif: RequestBody,
//        @Part("itemRefId") itemRefId: RequestBody,
//        @Part("itemRefType") itemRefType: RequestBody,
//        @Part("itemRefCaption") itemRefCaption: RequestBody,
//        @Path("workspaceId") workspaceId: String,
//        @Part imageFile: MultipartBody.Part
//    ): Call<ApiResponse<List<String>>>
//
//    @Multipart
//    @POST("images/{workspaceId}/multiple")
//    fun uploadMultipleImages(
//        @Path("workspaceId") workspaceId: String,
//        @Part("pointId") pointId: RequestBody,
//        @Part imageFiles: Array<MultipartBody.Part>
//    ): Call<ApiResponse<List<SaveImageResponse>>>
//
//    @Multipart
//    @POST("images/{imageId}/upload-annotated")
//    fun uploadAnnotatedImageFile(
//        @Part("pointId") pointId: RequestBody,
//        @Path("imageId") workspaceId: String,
//        @Part imageFile: MultipartBody.Part
//    ): Call<ApiResponse<String>>
//
//    @GET("images/{imageId}/file/size/square/200")
//    fun downloadImageThumbFile(@Path("imageId") imageId: String): Call<ResponseBody>
//
//    @GET("images/{imageId}/file/size/bounded/1200")
//    fun downloadImageLargeSize(@Path("imageId") imageId: String): Call<ResponseBody>
//
//    @GET("/api/v1/video/{videoId}/file")
//    fun downloadVideo(@Path("videoId") videoID: String): Call<ResponseBody>
//
//    @GET
//    @Streaming
//    fun downloadDocument(@Url fileUrl: String): Call<ResponseBody>
//
//    @Multipart
//    @POST("video/{workspaceId}?updatePoint=true")
//    fun uploadVideo(
//        @Part("itemRefId") itemRefId: RequestBody,
//        @Part("itemRefType") itemRefType: RequestBody,
//        @Part("itemRefCaption") itemRefCaption: RequestBody,
//        @Path("workspaceId") workspaceId: String,
//        @Part imageFile: MultipartBody.Part
//    ): Call<ApiResponse<Video>>
//
//    @Multipart
//    @POST("video/{workspaceId}?updatePoint=false")
//    fun uploadVideoFile(
//        @Part("itemRefId") itemRefId: RequestBody,
//        @Part("itemRefType") itemRefType: RequestBody,
//        @Part("itemRefCaption") itemRefCaption: RequestBody,
//        @Path("workspaceId") workspaceId: String,
//        @Part imageFile: MultipartBody.Part
//    ): Call<ApiResponse<Video>>
//
//    @Multipart
//    @POST("video/{workspaceId}?updatePoint=true")
//    fun uploadQuickVideoFile(
//        @Part("itemRefId") itemRefId: RequestBody,
//        @Part("itemRefType") itemRefType: RequestBody,
//        @Part("itemRefCaption") itemRefCaption: RequestBody,
//        @Path("workspaceId") workspaceId: String,
//        @Part imageFile: MultipartBody.Part
//    ): Call<ApiResponse<Video>>
//
//    /* *********************************** Tags ******************/
//    @GET("tags/{workspaceId}")
//    fun getAllTags(@Path("workspaceId") workspaceId: String): Call<ApiResponse<List<TagDetail>>>
//
//    /* *********************************** Account ******************/
//    @GET("users/self")
//    fun getSelfAccount(): Call<ApiResponse<User>>
//
//    @GET("users/{id}")
//    fun getUserById(@Path("id") id: String): Call<ApiResponse<User>>
//
//    /* *********************************** Shares ******************/
//    @GET("share/target/{workspaceId}")
//    fun listSharesForWorkspace(@Path("workspaceId") workspaceId: String): Call<ApiResponse<List<ShareWrapper>>>
//
//    @POST("share")
//    fun createShare(@Body share: String): Call<ApiResponse<Share>>
//
//    @GET("workspace/{id}/users")
//    fun getAllWorkspaceUsers(@Path("id") id: String): Call<ApiResponse<List<Assignee>>>
//
//    /* *********************************** Notifications ******************/
//    @POST("firebase/token-registry")
//    fun addRegisterToken(@Body share: RequestBody): Call<ApiResponse<Void>>
//
//    @DELETE("firebase/token-registry")
//    fun deleteRegisterToken(
//        @Query("deviceId") deviceId: String,
//        @Query("firebaseToken") firebaseToken: String
//    ): Call<ApiResponse<Void>>
//
//    @GET("push-notifications/all")
//    fun getPushNotifications(@Query("offset") id: Int): Call<ApiResponse<List<PushNotificationEntity>>>
//
//    @PUT("push-notifications/user-unread-status")
//    fun markNotificationsOpened(): Call<ApiResponse<Void>>
//
//    @PUT("push-notifications/{notificationId}")
//    fun markNotificationAsRead(
//        @Path("notificationId") notificationId: String,
//        @Query("markAsRead") markAsRead: Boolean
//    ): Call<ApiResponse<Void>>
//
//    @GET("video/itemref/{itemRefId}")
//    fun getVideoList(@Path("itemRefId") id: String): Call<ApiResponse<List<Video>>>
//
//    @GET("status")
//    fun validateBaseUrl(): Call<ApiResponse<Boolean>>
//
//    /* *********************************** Logged API ****************************/
//    @GET("auth/logged")
//    fun getLogged(
//        @Query("token") token: String,
//        @Query("email") email: String
//    ): Call<ApiResponse<AuthResult>>
//
//    /* *********************************** Document API ****************************/
//    @GET("document/itemref/{defectId}")
//    fun getDocumentListFromDefect(@Path("defectId") defectId: String): Call<ApiResponse<List<DocumentFile>>>
//
//    @GET("document/{documentId}/s3-link")
//    fun downloadDocumentByID(@Path("documentId") documentId: String): Call<DocumentDetail>
}
