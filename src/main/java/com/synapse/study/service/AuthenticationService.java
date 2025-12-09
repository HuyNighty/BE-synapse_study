package com.synapse.study.service;

import com.nimbusds.jose.JOSEException;
import com.synapse.study.dto.request.AuthenticationRequest;
import com.synapse.study.dto.request.IntrospectRequest;
import com.synapse.study.dto.request.LogoutRequest;
import com.synapse.study.dto.request.RefreshRequest;
import com.synapse.study.dto.response.AuthenticationResponse;
import com.synapse.study.dto.response.IntrospectResponse;

import java.text.ParseException;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest request);
    IntrospectResponse introspect(IntrospectRequest request);
    void logout(LogoutRequest request) throws ParseException, JOSEException;
    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
}
