package world.hzq.easysql.test;

import world.hzq.easysql.resolve.sql.JDBCType;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class TestJDBC{
    public static void main(String[] args) throws SQLException {
        JDBCType type = JDBCType.SMALLINT;
        System.out.println(type.getName());
    }
}
class EasySQLDriver implements java.sql.Driver{

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        return null;
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return false;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}