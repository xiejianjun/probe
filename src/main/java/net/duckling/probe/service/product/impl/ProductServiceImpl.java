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
package net.duckling.probe.service.product.impl;

import java.util.List;

import net.duckling.probe.service.product.Product;
import net.duckling.probe.service.product.ProductService;

public class ProductServiceImpl implements ProductService {
	private ProductDAO mdao;

	public void setProductDao(ProductDAO mdao) {
		this.mdao = mdao;
	}

	@Override
	public int save(Product product) {
		return mdao.save(product);
	}

	@Override
	public void update(Product product) {
		mdao.update(product);
	}

	@Override
	public void remove(int productId) {
		mdao.remove(productId);
	}

	@Override
	public Product read(int productId) {
		return mdao.read(productId);
	}

	@Override
	public List<Product> readAll() {
		return mdao.readAll();
	}

	@Override
	public void removeAll() {
		mdao.removeAll();
	}

	@Override
	public Product findByName(String name) {
		return mdao.findByName(name);
	}
}
