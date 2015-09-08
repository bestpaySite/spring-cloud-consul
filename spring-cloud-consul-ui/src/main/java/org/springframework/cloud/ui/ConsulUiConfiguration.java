/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.ui;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.consul.ConsulProperties;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Spencer Gibb
 */
@Configuration
@EnableZuulProxy
public class ConsulUiConfiguration {

	@Autowired
	private ZuulProperties zuulProperties;

	@Autowired
	private ConsulProperties consulProperties;

	@Autowired
	private ZuulHandlerMapping zuulHandlerMapping;

	@PostConstruct
	public void init() {
		String url = String.format("http://%s:%s", consulProperties.getHost(),
				consulProperties.getPort());

		ZuulProperties.ZuulRoute route = new ZuulProperties.ZuulRoute("consulApi",
				"/v1/**", null, url, false, false);
		zuulProperties.getRoutes().put("consulApi", route);

		route = new ZuulProperties.ZuulRoute("consulUi",
				"/consul/**", null, url, true, false);
		zuulProperties.getRoutes().put("consulUi", route);

		zuulHandlerMapping.registerHandlers();
	}

	@Bean
	public ConsulUiProperties consulUiProperties() {
		return new ConsulUiProperties();
	}
}