package dev.marvel.qrcheckin.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

	@Bean
	@Profile("postgres")
	public DataSource postgresDataSource() {
		return DataSourceBuilder.create()
				.driverClassName("org.postgresql.Driver")
				.url("jdbc:postgresql://localhost:5432/qrcheckin")
				.username("pg")
				.password("pg")
				.build();
	}

	@Bean
	@Profile("!postgres")
	public DataSource embeddedDataSource() {
		return DataSourceBuilder.create()
				.driverClassName("org.h2.Driver")
				.url("jdbc:h2:file:~/qrcheckin")
				.username("sa")
				.password("sa")
				.build();
	}
}
