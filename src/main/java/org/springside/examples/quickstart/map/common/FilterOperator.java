package org.springside.examples.quickstart.map.common;

public class FilterOperator {
	public enum Operator {
		EQ, LIKE, GT, LT, GTE, LTE,NEQ,BETWEEN,IN
	}
	public String querySubString;
	public String fieldName;
	public Object value;
	public Operator operator;
	public FilterOperator(String filedName, Operator operator, Object value) {
		this.fieldName = filedName;
		this.value = value;
		this.operator = operator;
		switch(operator) {
			case EQ:
				this.querySubString = filedName + "=" + value;
				break;
			case NEQ:
				this.querySubString = filedName + "!=" + value;
				break;
			case LIKE:
				this.querySubString = filedName + "%value%";
				break;
			case GT:
				this.querySubString = filedName + ">" + value;
				break;
			case LT:
				this.querySubString = filedName + "<" + value;
				break;
			case GTE:
				this.querySubString = filedName + ">=" + value;
				break;
			case LTE:
				this.querySubString = filedName + "<=" + value;
				break;
			case BETWEEN:
				this.querySubString = filedName + " between " + value;
				break;
			case IN:
				this.querySubString = filedName + " in " + value;
				break;
		}
	}
}
