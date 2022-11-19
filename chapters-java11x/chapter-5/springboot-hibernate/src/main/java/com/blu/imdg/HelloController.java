package com.blu.imdg;

import com.blu.imdg.IEmpService;
import com.blu.imdg.dto.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("emp")
public class HelloController {

    @Autowired
    private IEmpService empService;

    @RequestMapping("/")
    public String index(){
        return "Helllo from Apache Ignite";
    }

    //@GetMapping("article/{ename}")
    @RequestMapping(value = "/ename/{name}", method = GET)
    public ResponseEntity<List<Employee>> getEmployeeByName(@PathVariable("name")String ename){

        return new ResponseEntity<List<Employee>>(empService.getEmpByName(ename), HttpStatus.OK);

    }
}
