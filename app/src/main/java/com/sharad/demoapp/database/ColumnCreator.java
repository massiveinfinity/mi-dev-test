package com.winjit.database;

public class ColumnCreator
	{
		private String ColumnName;
		private ColumnDataType ColumnDataType;
		private String ColumnDefaultValue;
		private boolean NotNull;
		private boolean PrimaryKey;

		public String getColumnName()
			{
				return ColumnName;
			}

		public void setColumnName(String columnName)
			{
				ColumnName = columnName;
			}

		public ColumnDataType getColumnDataType()
			{
				return ColumnDataType;
			}

		public void setColumnDataType(ColumnDataType columnDataType)
			{
				ColumnDataType = columnDataType;
			}

		public String getColumnDefaultValue()
			{
				return ColumnDefaultValue;
			}

		public void setColumnDefaultValue(String columnDefaultValue)
			{
				ColumnDefaultValue = columnDefaultValue;
			}

		public boolean isNotNull()
			{
				return NotNull;
			}

		public void setNotNull(boolean notNull)
			{
				NotNull = notNull;
			}

		public boolean isPrimaryKey()
			{
				return PrimaryKey;
			}

		public void setPrimaryKey(boolean primaryKey)
			{
				PrimaryKey = primaryKey;
			}

	}
