package com.cdroho.webservice.webserviceimpl;

import com.cdroho.controller.PatientController;
import com.cdroho.logicalrepository.RoomRepository;
import com.cdroho.modle.dto.ResultDto;
import com.cdroho.modle.dto.WbsDto;
import com.cdroho.service.WaitQueueService;
import com.cdroho.utils.FastJsonConvertUtil;
import com.cdroho.webservice.WebServiceDemoService;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.jws.WebService;
import java.util.List;


@Service
@WebService(serviceName = "WebServiceDemoService", // 与接口中指定的name一致
        targetNamespace = "http://webservice.cdroho.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.cdroho.webservice.WebServiceDemoService" // 接口地址
)
public class WebServiceDemoServiceImpl implements WebServiceDemoService {

    private Logger log = LoggerFactory.getLogger(WebServiceDemoServiceImpl.class);
    @Autowired
    private WaitQueueService queueService;
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public String queryMsg(String sickCode) {
        String string = sickCode;
        ResultDto o=new ResultDto();
        o=FastJsonConvertUtil.convertJSONToObject(string,ResultDto.class);
        log.info("就诊卡号为----->"+o.getSickCode());
        List<Object> objects=roomRepository.findKsData(o.getSickCode());
        log.info("list长度为----->"+objects.size());
        String msg=objToDto(objects);
        return msg;
    }

    public String objToDto(List<Object> objects){
        WbsDto wbsDto=new WbsDto();
        if (objects.size()>0) {
            for(Object o:objects){
                Object[] oArray=(Object[]) o;
                wbsDto.setCxm(String.valueOf(oArray[8]));
                wbsDto.setCksmc(String.valueOf(oArray[4]));
                wbsDto.setCzsmc(String.valueOf(oArray[6]));
                wbsDto.setCysmc(String.valueOf(oArray[7]));
                wbsDto.setIqmjzrssy(Integer.valueOf(String.valueOf(oArray[2])));
                if (oArray[5]!=null) {
                    wbsDto.setIdqxh(Integer.valueOf(String.valueOf(oArray[5])));
                }
                wbsDto.setLdlxhl(Integer.valueOf(String.valueOf(oArray[0])));
                wbsDto.setResultCode("0");
                wbsDto.setResultMessage("查询成功！");
            }
        }else{
            wbsDto.setResultCode("1");
            wbsDto.setResultMessage("查询失败！医生未登录");
        }
        return FastJsonConvertUtil.convertObjectToJSON(wbsDto);
    }
}
