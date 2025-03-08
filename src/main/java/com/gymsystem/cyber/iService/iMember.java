package com.gymsystem.cyber.iService;

import com.gymsystem.cyber.model.Request.MemberRegistrationRequest;
import com.gymsystem.cyber.model.ResponseObject;


public interface iMember {

    ResponseObject registerMember(MemberRegistrationRequest member);

    ResponseObject getAllMembers();
}
