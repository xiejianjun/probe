/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package net.duckling.probe.ui.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.probe.service.product.Product;
import net.duckling.probe.service.product.ProductService;
import net.duckling.probe.ui.permission.annotation.RequirePermission;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 监测产品管理
 * @author xiejj@cnic.cn
 *
 */
@Controller
@RequestMapping("/product.do")
@RequirePermission(operation="admin")
public class ProductController {

	@Autowired
	private ProductService productService;
	/**
	 * 添加监测产品
	 * @param name	产品名称
	 * @return
	 */
	@RequestMapping(params = "act=add")
	public String add(@RequestParam("name") String name,@RequestParam("description") String description) {
		Product product = productService.findByName(name);
		if (product == null) {
			product = new Product();
			product.setName(name);
			product.setDescription(description);
			productService.save(product);
		}
		return "redirect:product.do";
	}
	
	
	/**
	 * 获取产品的信息
	 * @param productId		产品ID
	 * @return
	 */
	@RequestMapping(params = "act=getProduct", method = RequestMethod.GET)
	public void getCollector(@RequestParam("productId")int productId,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject object=new JSONObject();	
		object.put("productId", productId);
		Product product = productService.read(productId);	
		object.put("name", product.getName());
		object.put("description",product.getDescription());
		try {
	        //设置页面不缓存
	        response.setContentType("application/json");
	        response.setHeader("Pragma", "No-cache");
	        response.setHeader("Cache-Control", "no-cache");
	        response.setCharacterEncoding("UTF-8");
	        PrintWriter out= null;
	        out = response.getWriter();
	        out.print(object.toJSONString());
	        out.flush();
	        out.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }		
	}
	/**
	 * 修改产品的信息
	 * @param productId		产品ID
	 * @return
	 */
	@RequestMapping(params = "act=ModifyProduct")
	public String ModifyCollector(@RequestParam("productId")int productId,
			@RequestParam("description")String description,
			@RequestParam("name")String name) {
		Product product = productService.read(productId);	
		if(product!=null){
			product=new Product();
			product.setDescription(description);
			product.setName(name);
			product.setId(productId);
			productService.update(product);
		}
		
		
		return "redirect:product.do";
	}
	
	
	
	/**
	 * 删除监测产品
	 * @param productId	产品的ID
	 * @return
	 */
	@RequestMapping(params = "act=del")
	public String del(@RequestParam("productId") int productId) {
		productService.remove(productId);
		return "redirect:product.do";
	}
	/**
	 * 显示所有现存的监测产品
	 * @param model
	 * @return
	 */
	@RequestMapping
	public String home(Model model) {
		model.addAttribute("products", productService.readAll());
		return "product";
	}
}
