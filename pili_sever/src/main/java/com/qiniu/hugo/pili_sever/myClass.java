package com.qiniu.hugo.pili_sever;

import com.qiniu.pili.Client;
import com.qiniu.pili.Hub;
import com.qiniu.pili.Meeting;

import java.util.Date;

public class myClass {
    // Replace with your keys here
    private static final String ACCESS_KEY = "Ib4CRWyIRVGHilI8ECRjlilTUsrUUvVah5jjtVPg"; //填入你自己的ACCESS_KEY
    private static final String SECRET_KEY = "nUEVMeT9xVhwOZdb2nKQc2jno7hyAM_0nf26LEbC";  //填入你自己的SECRET_KEY
    // Replace with your hub name
    private static final String HUB_NAME = "lipengv2"; // 填入你们自己的直播空间名称
    private static Meeting meeting;
    public static void main(String[] args ){

        // Instantiate an Hub object
        Client credentials = new Client(ACCESS_KEY, SECRET_KEY); // Credentials Object
        //初始化Hub
        Hub hub = credentials.newHub(HUB_NAME);
        meeting = credentials.newMeeting();
        //meeting = new Meeting(credentials); XXXX
        //meeting.createRoom()

        // create room with name
        try {
            String roomName = "3000101";
            String r1 =  meeting.createRoom("3000101",roomName,12);


            Meeting.Room room = meeting.getRoom(roomName);
            System.out.println("roomName:"+r1);


        } catch (Exception e){
            e.printStackTrace();
        }


        try {
            String token = meeting.roomToken("3000101", "3000101", "admin", new Date(15285152380000L));
            System.out.println("token==>  "+token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

