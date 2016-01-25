package com.winjit.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
	{

		private static DatabaseHelper sInstance;
		private SQLiteDatabase sqLiteDatabase;
		private static final String DATABASE_NAME = "database_name";
		private static final int DATABASE_VERSION = 1;
		private String tag = DatabaseHelper.class.getName();
		Context context;

		public static synchronized DatabaseHelper getInstance(Context context)
			{
				
				if (sInstance == null)
					{
						sInstance = new DatabaseHelper(context.getApplicationContext());
					}
				return sInstance;
			}

		/****
		 * Clear singleton database instance
		 */
		public static void clearInstance()
			{
				if (sInstance != null)
					{
						sInstance = null;
					}
			}

		/**
		 * Conuctor should be private to prevent direct instantiation.
		 * make call to static method "getInstance()" instead.
		 */
		private DatabaseHelper(Context context)
			{
				super(context, DATABASE_NAME, null, DATABASE_VERSION);
			}

		@Override
		public void onCreate(SQLiteDatabase db)
			{
				// TODO Auto-generated method stub

			}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
			{
				// TODO Auto-generated method stub

			}

		/**
		 * Drop existing table(if present with same name) and create new table.
		 * @param tableCreator
		 * @throws Exception
		 */
		public void createTable(TableCreator tableCreator) throws Exception
			{
				openDatabaseForWrite();
				try
					{
						if (tableCreator.getTableName() != null && !tableCreator.getTableName().isEmpty())
							{
								sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tableCreator.getTableName());

								StringBuilder ingBuilder = new StringBuilder();
								ingBuilder.append("CREATE TABLE " + tableCreator.getTableName());
								ingBuilder.append("(");
								if (tableCreator.getColumnCreatorArrayList() != null && tableCreator.getColumnCreatorArrayList().size() > 0)
									{
										//traverse through is each column
										for (ColumnCreator columnCreator : tableCreator.getColumnCreatorArrayList())
											{
												if (columnCreator.getColumnName() != null && !columnCreator.getColumnName().isEmpty())
													{

														ingBuilder.append(columnCreator.getColumnName());
														ingBuilder.append(" ");

														if (columnCreator.getColumnDataType() == ColumnDataType.INTEGER)
															{
																ingBuilder.append("INTEGER");
															}
														else if (columnCreator.getColumnDataType() == ColumnDataType.REAL)
															{
																ingBuilder.append("REAL");
															}
														else if (columnCreator.getColumnDataType() == ColumnDataType.TEXT)
															{
																ingBuilder.append("TEXT");
															}
														else
															{
																throw new DatabaseHelperException("DataType for column \'"
																		+ columnCreator.getColumnName() + "\' is not specified");
															}
														ingBuilder.append(" ");

														if (columnCreator.isPrimaryKey())
															{
																ingBuilder.append("PRIMARY KEY");
															}
														ingBuilder.append(" ");

														if (columnCreator.isNotNull())
															{
																ingBuilder.append("NOT NULL");
															}

														ingBuilder.append(" ");
														ingBuilder.append(",");
													}
												else
													{
														throw new DatabaseHelperException(
																"Column name cannot be NULL or Empty.Please use valid column name");
													}

											}

									}
								else
									{
										throw new DatabaseHelperException("Cannot create empty table \'" + tableCreator.getTableName()
												+ "\', you should specify atleast one column");

									}

								ingBuilder.append(")");
								String queryString = ingBuilder.toString();
								queryString = queryString.replaceAll(" ,", ",");
								queryString = queryString.replaceAll(",\\)", ")");
								Log.d(tag, "" + queryString);

								sqLiteDatabase.execSQL(queryString);

							}
						else
							{
								throw new DatabaseHelperException("Table name cannot be NULL or Empty.Please use valid table name");
							}

					}
				finally
					{
						closeDatabase();
					}
			}

		public void replaceAll(StringBuilder builder, String from, String to)
			{
				int index = builder.indexOf(from);
				while (index != -1)
					{
						builder.replace(index, index + from.length(), to);
						index += to.length(); // Move to the end of the replacement
						index = builder.indexOf(from, index);
					}
			}

		/**
		 * get Readable instance of sqLiteDatabase
		 * 
		 * @throws SQLException
		 */
		public synchronized void openDatabaseForRead()
			{
				// Open the sqLiteDatabase
				try
					{
						if (sqLiteDatabase == null || !sqLiteDatabase.isOpen())
							{

								sqLiteDatabase = this.getReadableDatabase();

							}
						else
							{
							}
					}
				catch (Exception e)
					{
						e.printStackTrace();
					}
			}

		/**
		 * get Writable instance of sqLiteDatabase
		 * 
		 * @throws SQLException
		 */
		public synchronized void openDatabaseForWrite()
			{
				// Open the sqLiteDatabase
				try
					{
						if (sqLiteDatabase == null || !sqLiteDatabase.isOpen())
							{
								sqLiteDatabase = this.getWritableDatabase();
							}
						else
							{
								// if the current mode is read only;open db in write mode
								if (sqLiteDatabase.isReadOnly())
									{
										sqLiteDatabase = this.getWritableDatabase();
									}
							}
					}
				catch (Exception e)
					{
						e.printStackTrace();
					}
			}

		public synchronized void closeDatabase()
			{
				super.close();
				try
					{
						if (sqLiteDatabase != null)
							sqLiteDatabase.close();
					}
				catch (Exception e)
					{
						e.printStackTrace();
					}
			}

		/**
		 * Insert single record in table.
		 * 
		 * @param TableName
		 *            table name in which record to be inserted
		 * @param values
		 *            column values
		 * @return true if record is inserted
		 */
		public boolean InsertRecord(String TableName, ContentValues values)
			{
				long lid = -1;

				try
					{
						openDatabaseForWrite();
						lid = sqLiteDatabase.insert(TableName, null, values);

					}
				catch (Exception e)
					{
						lid = -1;
						e.printStackTrace();
					}

				return lid == -1 ? false : true;

			}

		/**
		 * update single record in table
		 * 
		 * @param TableName
		 *            table name in which record to be inserted
		 * @param values
		 *            column values
		 * @param WhereClause
		 *            where clause for primary key
		 * @return true if record is updated
		 */
		public boolean UpdateRecord(String TableName, ContentValues values, String WhereClause, String[] args)
			{
				long lid = -1;
				try
					{
						openDatabaseForWrite();
						lid = sqLiteDatabase.update(TableName, values, WhereClause, args);

					}
				catch (Exception e)
					{
						lid = -1;
						e.printStackTrace();
					}
				finally
					{
						closeDatabase();
					}
				return lid == -1 ? false : true;
			}

		/**
		 * Delete a single record
		 * 
		 * @param TableName
		 *            table name from which record to delete
		 * @param WhereClause
		 *            primary key which record to delete
		 */
		public boolean deleteRecord(String TableName, String WhereClause, String[] args)
			{

				int affectedRows = 0;
				try
					{
						openDatabaseForWrite();
						affectedRows = sqLiteDatabase.delete(TableName, WhereClause, args);

					}
				catch (Exception e)
					{
						e.printStackTrace();

					}
				finally
					{
						closeDatabase();
					}
				return affectedRows == 0 ? false : true;
			}

		/**
		 * update single record in table
		 *  @param Query Query to execute
		 */
		public void UpdateRecordByQuery(String Query, String[] args)
			{
				Cursor cursor;
				try
					{
						openDatabaseForWrite();
						cursor = sqLiteDatabase.rawQuery(Query, args);
						cursor.moveToFirst();
						cursor.close();
					}
				catch (Exception e)
					{
						e.printStackTrace();

					}
				finally
					{
						closeDatabase();
					}
			}
		
		
		
		
		
		/**
		 * This method export database to SDcard
		 * 
		 * @return true if exported
		 */
		public boolean exportDBToSDcard()
			{
				 String strDB_path = File.separator + "data" + File.separator + "data" + File.separator + "com.example.databaseutils" + File.separator + "databases"
							+ File.separator;
				FileChannel source = null;
				FileChannel destination = null;
				File currentDB = new File(strDB_path + DATABASE_NAME);
				File backupDB = new File(Environment.getExternalStorageDirectory() + File.separator + "AirtouchExportDB.sdf");
				try
					{
						source = new FileInputStream(currentDB).getChannel();
						destination = new FileOutputStream(backupDB).getChannel();
						destination.transferFrom(source, 0, source.size());
						source.close();
						destination.close();
						return true;
					}
				catch (IOException e)
					{
						e.printStackTrace();
					}
				return false;
			}
		
		
		

	}
