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
package net.duckling.probe.service.product;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;

import net.duckling.probe.service.product.Product;
import net.duckling.probe.service.product.ProductService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:root-context.xml")
public class ProductServiceTest {
    @Resource
    private ProductService productService;
    private int procuctId;
	@Before
	public void setUp(){
		Product product = new Product();
		product.setName("CSP监测");
		procuctId= productService.save(product);
	}
	@After
	public void tearDown(){
		productService.remove(procuctId);
	}
	@Test
	public void testSave() {
		Product product = new Product();
		product.setName("DDL监测");
		int productId = productService.save(product);
		Product readed = productService.read(productId);
		assertNotNull(readed);
		assertEquals("DDL监测",readed.getName());
	}

	@Test
	public void testUpdate() {
		Product product = this.productService.read(procuctId);
		product.setName("CSP监控1");
		productService.update(product);
		product = productService.read(procuctId);
		assertNotNull(product);
	}


	@Test
	public void testReadAll() {
		productService.removeAll();
		List<Product> products = productService.readAll();
		assertNotNull(products);
		assertEquals(0, products.size());
	}

}
