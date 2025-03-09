package com.gymsystem.cyber.iService;

import com.gymsystem.cyber.model.Request.MemberRegistrationRequest;
import com.gymsystem.cyber.model.Request.PTforUserRequest;
import com.gymsystem.cyber.model.Request.PTscheduleRequest;
import com.gymsystem.cyber.model.ResponseObject;

import javax.security.auth.login.AccountNotFoundException;


public interface iMember {

    ResponseObject registerMember(MemberRegistrationRequest member);

    ResponseObject getAllMembers();

    ResponseObject regisPTForUser(PTforUserRequest pTforUserRequest) throws AccountNotFoundException;

    ResponseObject regisPTForSchedule(PTscheduleRequest pTscheduleRequest) throws AccountNotFoundException;
}
