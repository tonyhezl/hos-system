package com.cdroho.webservice;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


@WebService
public interface WebServiceDemoService {

    @WebMethod
    String queryMsg(@WebParam(name = "sickCode")String sickCode);

}
