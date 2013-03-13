package com.senacor.hd12.support;

import java.util.List;

import com.google.common.collect.Lists;
import com.senacor.hd12.neo.model.Person;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class CreateTestDatabase {

	// cloud
	public static final String URL = "http://hd12.network.cloudfoundry.com/rest";
	// lokal
//	public static final String URL = "http://localhost:8080/rest";

	public static void main(String[] args) {
		final String baseUrl;
		if (args.length > 0) {
			baseUrl = args[args.length - 1];

		} else {
			baseUrl = URL;
		}

		System.out.println("Starting update of database: " + baseUrl);

		final RestTemplate restTemplate = new RestTemplate();
		final List<HttpMessageConverter<?>> messageConverters = Lists.<HttpMessageConverter<?>>newArrayList(
			new StringHttpMessageConverter(), 
			new MappingJacksonHttpMessageConverter());
		
		restTemplate.setMessageConverters(messageConverters);

		Person person1 = new Person("Oleg", "Galimov", "ogalimov", "https://www.xing.com/pubimg/users/1/c/e/a5b298316.11477632,2.jpg");
		Person person2 = new Person("Stephan", "Palm", "spalm", "https://www.xing.com/pubimg/users/5/5/c/eea37375a.7835381,1.jpg");
		Person person3 = new Person("Andreas", "Keefer", "akeefer", "https://www.xing.com/img/users/2/a/7/d6c4f4d1a.6031977,1.jpg");
		Person person4 = new Person("Henning", "Stamminger", "hstamminger", "https://www.xing.com/img/users/c/4/6/1b62f438c.5799921,3.jpg");
		Person person5 = new Person("Ralph", "Winzinger", "rwinzinger", "https://www.xing.com/img/users/7/9/7/86aa2438d.7678740,2.jpg");
		Person person6 = new Person("Victor", "Volle", "vvolle", "https://www.xing.com/pubimg/users/1/6/a/659d5d236.3304913,2.jpg");
		Person person7 = new Person("Nikolay", "Georgiev", "ngeorgiev", "https://www.xing.com/img/users/7/1/e/58210f841.16295985,3.jpg");
		Person person8 = new Person("Markus", "Wimmer", "mwimmer", "http://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Gnome-stock_person.svg/380px-Gnome-stock_person.svg.png");
		Person person9 = new Person("Michael", "Brunner", "mbrunner", "https://www.xing.com/img/users/2/3/7/15104f18c.6512640,3.jpg");
		Person person10 = new Person("Charles-Tom", "Kallepally", "ctk", "https://www.xing.com/pubimg/users/a/f/7/fa8853f38.5765073,2.jpg");

		restTemplate.put(baseUrl + "/person/ogalimov", person1);
		restTemplate.put(baseUrl + "/person/spalm", person2);
		restTemplate.put(baseUrl + "/person/akeefer", person3);
		restTemplate.put(baseUrl + "/person/hstamminger", person4);
		restTemplate.put(baseUrl + "/person/rwinzinger", person5);
		restTemplate.put(baseUrl + "/person/vvolle", person6);
		restTemplate.put(baseUrl + "/person/ngeorgiev", person7);
		restTemplate.put(baseUrl + "/person/mwimmer", person8);
		restTemplate.put(baseUrl + "/person/mbrunner", person9);
		restTemplate.put(baseUrl + "/person/ctk", person10);

		restTemplate.put(baseUrl + "/person/hstamminger/peers/spalm", "");
		restTemplate.put(baseUrl + "/person/hstamminger/peers/rwinzinger", "");
		restTemplate.put(baseUrl + "/person/hstamminger/peers/akeefer", "");
		restTemplate.put(baseUrl + "/person/ogalimov/peers/spalm", "");
		restTemplate.put(baseUrl + "/person/ogalimov/peers/akeefer", "");

		restTemplate.put(baseUrl + "/person/spalm/peers/ogalimov", "");
		restTemplate.put(baseUrl + "/person/spalm/peers/akeefer", "");
		restTemplate.put(baseUrl + "/person/spalm/peers/hstamminger", "");
		restTemplate.put(baseUrl + "/person/spalm/peers/rwinzinger", "");
		restTemplate.put(baseUrl + "/person/spalm/peers/ngeorgiev", "");
		restTemplate.put(baseUrl + "/person/spalm/peers/vvolle", "");
		restTemplate.put(baseUrl + "/person/spalm/peers/mbrunner", "");
		restTemplate.put(baseUrl + "/person/spalm/peers/mwimmer", "");
		restTemplate.put(baseUrl + "/person/spalm/peers/mbrunner", "");
		restTemplate.put(baseUrl + "/person/spalm/peers/ctk", "");

		restTemplate.put(baseUrl + "/person/ngeorgiev/peers/spalm", "");
		restTemplate.put(baseUrl + "/person/hstamminger/peers/spalm", "");
		restTemplate.put(baseUrl + "/person/ogalimov/peers/spalm", "");
		restTemplate.put(baseUrl + "/person/vvolle/peers/spalm", "");
		restTemplate.put(baseUrl + "/person/rwinzinger/peers/spalm", "");
		restTemplate.put(baseUrl + "/person/akeefer/peers/spalm", "");
		restTemplate.put(baseUrl + "/person/mwimmer/peers/spalm", "");
		restTemplate.put(baseUrl + "/person/ctk/peers/spalm", "");
		restTemplate.put(baseUrl + "/person/mbrunner/peers/spalm", "");

		restTemplate.put(baseUrl + "/person/akeefer/organisation/Senacor", "");
		restTemplate.put(baseUrl + "/person/spalm/organisation/Senacor", "");
		restTemplate.put(baseUrl + "/person/ogalimov/organisation/Senacor", "");

		System.out.println("Finished update of database.");
	}

}
