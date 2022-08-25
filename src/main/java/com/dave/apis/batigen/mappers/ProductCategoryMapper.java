package com.dave.apis.batigen.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.dave.apis.batigen.core.ProductCategory;

public class ProductCategoryMapper implements ResultSetMapper<ProductCategory> {

	@Override
	public ProductCategory map(int index, ResultSet rs, StatementContext ctx) throws SQLException {

		ProductCategory p = new ProductCategory();
		p.setCategoryId(rs.getInt("CATEGORY_ID"));
		p.setCategory(rs.getString("CATEGORY"));
		p.setDescription(rs.getString("DESCRIPTION"));
		return p;
	}
}
