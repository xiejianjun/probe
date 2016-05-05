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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import net.duckling.probe.common.BaseDao;
import net.duckling.probe.service.product.Product;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class ProductDAO extends BaseDao {
	private RowMapper<Product> rowMapper = new RowMapper<Product>() {
		@Override
		public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
			Product product = new Product();
			product.setId(rs.getInt("id"));
			product.setName(rs.getString("name"));
			product.setDescription(rs.getString("description"));
			return product;
		}
	};

	public int save(final Product product) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				String sql = "insert into product(name,description) values(?,?)";
				PreparedStatement pst = con.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);
				try {
					pst.setString(1, product.getName());
					pst.setString(2, product.getDescription());
				} catch(SQLException e) {
					pst.close();
					throw e;
				}
				return pst;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	public void update(Product product) {
		getJdbcTemplate().update("update product set name=? ,description=? where id=?",
				new Object[] { product.getName(),product.getDescription(), product.getId() });
	}

	public void remove(int productId) {
		getJdbcTemplate().update("delete p ,c from product as p ,collector as c   where p.id=? and c.productId=p.id;",
				new Object[] { productId });
		getJdbcTemplate().update("delete  from product  where id=? ;",
				new Object[] { productId });
	}

	public Product read(int productId) {
		List<Product> products = getJdbcTemplate().query(
				"select * from product where id=?", new Object[] { productId },
				rowMapper);
		if (products.size() > 0) {
			return products.get(0);
		} else {
			return null;
		}
	}

	public List<Product> readAll() {
		return getJdbcTemplate().query("select * from product", rowMapper);
	}

	public void removeAll() {
		getJdbcTemplate().update("delete from product");
	}

	public Product findByName(String name) {
		List<Product> products = getJdbcTemplate().query(
				"select * from product where name=?", new Object[] { name },
				rowMapper);
		if (products.size() > 0) {
			return products.get(0);
		} else {
			return null;
		}
	}
}
