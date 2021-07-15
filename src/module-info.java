module compileMD {
	requires java.sql;
	requires ormlite.core;
	requires ormlite.jdbc;
	exports orm to ormlite.core;
}