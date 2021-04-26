package com.nmsl.service;

import com.nmsl.entity.Comment;
import com.nmsl.entity.system.Request;

import java.util.List;

/**
 * @Author Paracosm
 * @Date 2021/1/25 14:27
 * @Version 1.0
 */
public interface RequestService {

    Request saveLog(Request request);

    List<Request> listRequest();

    int logNum();

    boolean truncateLog ();
}
