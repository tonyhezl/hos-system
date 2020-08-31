package com.cdroho.controller;

import com.cdroho.domaindto.DomainRuleDto;
import com.cdroho.eventlistenser.PublishEvents;
import com.cdroho.logicalrepository.TriageRepository;
import com.cdroho.modle.NurseTriage;
import com.cdroho.modle.Person;
import com.cdroho.logicalrepository.UserRepository;
import com.cdroho.modle.dto.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import sun.security.krb5.internal.crypto.NullEType;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 使用@RestController注解，返回的不是视图页面
 * 想返回json字符串就用RestController
 * @author HZL
 *
 */
@RestController
@RequestMapping("query")
@SuppressWarnings("unchecked")
public class HibernateTestController {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TriageRepository triageRepository;

	@Autowired
	private DomainRuleDto dto;

	/*@Autowired
	private TestService testService;*/

	@Autowired
	PublishEvents publishEvents;

	public static Map<String,Object> resultMap=new HashMap();

	@RequestMapping("find")
	public Map testHibernate() {
		
		Person person = new Person();
		
		person.setName("张三");
		
		Iterable itme=userRepository.findAll();

		resultMap.put("code",0);

		resultMap.put("msg","");

		resultMap.put("count","15");

		while(itme.iterator().hasNext()) {
			//layui的table数据格式必须严格遵守官方要求的JSON数据格式
			Person p=(Person) itme.iterator().next();
			Object[] o=new Object[1];
			o[0]=p;
			resultMap.put("data",o);

			return resultMap;
		}


		return null;
	}
	
	@RequestMapping("domainRuleDto")
	public Person testHibernateOne(@RequestParam(value="name") String name) {
		
		List<Person> result = userRepository.findByNameList(name);
		
		return userRepository.findByName(name);

	}
	
	@RequestMapping("findTwo")
	public Person testHibernateTwo(@RequestParam(value="name") String name,@RequestParam(value="age") int age) {
		
		Person result = userRepository.findByNamedParam(age,name);
		
		return result;
		
	}
	
	@RequestMapping("findThr")
	public List<Person> testHibernateThr(@RequestParam(value="name") String name) {
		
		List<Person> persons = userRepository.findByNameMatch(name);
		
		return persons;
		
	}
	
	/**
	 * new PageRequest()废弃，代替方式如下
	 * page 页数，从0页开始
	 * size 每页显示记录个数
	 * direction must not be.分页内容升序还是降序
	 * properties must not be.分页内容按某个字段升序还是降序
	 * @return
	 */
	@GetMapping("findPage")
	public List<Person> pageByPerson() {
		
		PageRequest page=PageRequest.of( 2, 2, Sort.Direction.ASC,"age");

		return userRepository.findAll(page).getContent();
		
	}

	@RequestMapping("queryRule")
	public DomainRuleDto testQueryRule(@RequestParam(value = "ip") String ip){
		List<Object[]> list = userRepository.findRuleByIp(ip);
		dto.setReturn_flag_step((int)list.get(0)[0]);
		dto.setLate_flag_step((int)list.get(0)[1]);
		return dto;
	}

	/*@GetMapping("/hello")
	public void useHello() {
		 testService.testDubbo("dubbo");
		System.out.println("ok");
	}*/


	@GetMapping("/event")
	public void testEvent(){
		publishEvents.punlishEvents();
		System.out.println("开始其他业务逻辑处理");
	}


	/**
	 * 返回分诊台数据
	 * @return
	 */
	@RequestMapping("queryTriage")
	public Iterable<NurseTriage> queryTriage() {
		return  triageRepository.findAll();
	}

	/**
	 * 新增分诊台
	 */
	@PostMapping(value = "addTriage")
	public Map addTriage(@RequestBody NurseTriage nurseTriage){
		Long triageId =nurseTriage.getId();
		if(triageId!=0){
				triageRepository.updateNurseTriageBy(nurseTriage.getName(),
						nurseTriage.getTriageIp(),nurseTriage.getTriageType(),nurseTriage.getLockCount(),
						nurseTriage.getPassCount(),nurseTriage.getPassWeight(),nurseTriage.getReturnWeight(),nurseTriage.getId());
				resultMap.put("code","success");
				resultMap.put("msg","修改成功");
				return resultMap;
		}else{
			if(triageRepository.findByName(nurseTriage.getName())!=null){
				resultMap.put("msg","当前分诊台已存在");
				resultMap.put("code","failed");
				return resultMap;
			}else{
				resultMap.put("code","success");
				resultMap.put("msg","保存成功");
				triageRepository.save(nurseTriage);
				return resultMap;
			}
		}
	}

	/**
	 * 删除分诊台
	 */
	@PostMapping(value = "deleteTriage")
	public Map deleteTriage(@RequestBody NurseTriage nurseTriage){
		NurseTriage triage = triageRepository.findByName(nurseTriage.getName());
		triageRepository.deleteNurseTriageBy(triage.getId());
		resultMap.put("code","success");
		return resultMap;
	}

	/**
	 * 初始化分诊台下拉框的值
	 * @return
	 */
	@RequestMapping("findAllTriage")
	public List<String> findAllTriage(){
		List<NurseTriage> triageList = new ArrayList<NurseTriage>();
		triageList=triageRepository.findAllNurse();
		List<String> triageNames = new ArrayList<String>();
		for(int i=0;i<triageList.size();i++){
			triageNames.add(triageList.get(i).getName());
		}
		return  triageNames;
	}

	/**
	 * 修改分诊台
	 */
	@PostMapping(value = "updateTriage")
	public Map updateTriage(@RequestBody NurseTriage nurseTriage){
		Long triageId =nurseTriage.getId();
		if(!(triageId.equals(""))){
			triageRepository.updateNurseTriageBy(nurseTriage.getName(),
					nurseTriage.getTriageIp(),nurseTriage.getTriageType(),nurseTriage.getLockCount(),
					nurseTriage.getPassCount(),nurseTriage.getPassWeight(),nurseTriage.getReturnWeight(),nurseTriage.getId());
			resultMap.put("code","success");
			return resultMap;
		}else {
			resultMap.put("code","failed");
			return resultMap;
		}
	}


}