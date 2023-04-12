package com.chriswilliams.cal_hope_c868.DB;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

//    ADMIN CALLS
    @FormUrlEncoded
    @POST("adminLogin")
    Call<AdminResponse> adminLogin(
      @Field("username") String username,
      @Field("password") String password
    );

    @FormUrlEncoded
    @POST("createAdmin")
    Call<DefaultResponse> createAdmin(
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String email,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("address") String address
    );

    @FormUrlEncoded
    @POST("updateAdmin/{id}")
    Call<AdminResponse> updateAdmin(
            @Path("id") int id,
            @Field("username") String username,
            @Field("email") String email,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("address") String address
    );

    @FormUrlEncoded
    @POST("updateAdminPassword")
    Call<DefaultResponse> updateAdminPassword(
            @Field("current_password") String currentPassword,
            @Field("new_password") String newPassword,
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("updateAdminUsername/{id}")
    Call<DefaultResponse> updateAdminUsername(
            @Path("id") int id,
            @Field("current_username") String currentUsername,
            @Field("new_username") String newUsername
    );

    @DELETE("deleteAdmin/{id}")
    Call<DefaultResponse> deleteAdmin(@Path("id") int id);

    @GET("allAdmins")
    Call<AdminResponse> getAllAdmins();

//    NURSE CALLS
    @FormUrlEncoded
    @POST("createNurse")
    Call<DefaultResponse> createNurse(
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String email,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("address") String address
    );

    @FormUrlEncoded
    @POST("nurseLogin")
    Call<NurseResponse> nurseLogin(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("updateNurse/{id}")
    Call<NurseResponse> updateNurse(
            @Path("id") int id,
            @Field("username") String username,
            @Field("email") String email,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("address") String address
    );

    @FormUrlEncoded
    @POST("updateNursePassword")
    Call<DefaultResponse> updateNursePassword(
            @Field("current_password") String currentPassword,
            @Field("new_password") String newPassword,
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("updateNurseUsername/{id}")
    Call<DefaultResponse> updateNurseUsername(
            @Path("id") int id,
            @Field("current_username") String currentUsername,
            @Field("new_username") String newUsername
    );

    @DELETE("deleteNurse/{id}")
    Call<DefaultResponse> deleteNurse(@Path("id") int id);

    @GET("allNurses")
    Call<AllNursesResponse> getAllNurses();

    @GET("nurseById/{id}")
    Call<NurseResponse> getNurseById(@Path("id") int id);

//    REQUEST CALLS
    @FormUrlEncoded
    @POST("createNurseRequest")
    Call<DefaultResponse> createNurseRequest(
            @Field("start_date") String startDate,
            @Field("end_date") String endDate,
            @Field("status") String status,
            @Field("type") String type,
            @Field("notes") String notes,
            @Field("nurse_id") int nurseId
    );

    @FormUrlEncoded
    @POST("updateNurseRequest/{id}")
    Call<DefaultResponse> updateNurseRequest(
            @Path("id") int id,
            @Field("start_date") String startDate,
            @Field("end_date") String endDate,
            @Field("status") String status,
            @Field("type") String type,
            @Field("notes") String notes
    );

    @GET("allNurseRequests")
    Call<RequestsResponse> getAllRequests();

    @GET("requestsByNurse/{nurse_id}")
    Call<RequestsResponse> getRequestsByNurse(
            @Path("nurse_id") int nurse_id
    );

    @GET("pendingRequestsByNurse/{nurse_id}")
    Call<RequestsResponse> getPendingRequestsByNurse(
            @Path("nurse_id") int nurse_id
    );

    @GET("requestsByStatus/{status}")
    Call<RequestsResponse> getRequestsByStatus(
            @Path("status") String status
    );

    @DELETE("deleteRequest/{id}")
    Call<DefaultResponse> deleteRequest(@Path("id") int id);

//    SHIFT CALLS

    @GET("shiftsByNurse/{nurse_id}")
    Call<ShiftsResponse> getShiftsByNurse(
            @Path("nurse_id") int nurse_id
    );

    @GET("upcomingShiftsByNurse/{nurse_id}")
    Call<ShiftsResponse> getUpcomingShiftsByNurse(
            @Path("nurse_id") int nurse_id
    );

    @GET("shiftsByUnit/{unit_id}")
    Call<ShiftsResponse> getShiftsByUnit(
            @Path("unit_id") int unit_id
    );

    @DELETE("deleteShift/{id}")
    Call<DefaultResponse> deleteShift(@Path("id") int id);

//    UNIT CALLS
    @GET("allUnits")
    Call<AllUnitsResponse> getAllUnits();

    @GET("unitById/{id}")
    Call<AllUnitsResponse> getUnitsById(
            @Path("id") int id
    );

    @GET("unitById/{id}")
    Call<UnitResponse> getUnitById(
            @Path("id") int id
    );
}
