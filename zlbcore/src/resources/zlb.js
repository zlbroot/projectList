zlb.scan ="com.test";
zlb.config.putAll({
	driver:"com.mysql.jdbc.Driver",
	url:"jdbc:mysql://127.0.0.1:3306/zlb?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&autoReconnect=true",
	username:"root",
	password:"billing"
});

zlb.prefix="/WEB-INF/views/";
zlb.suffix=".jsp"
