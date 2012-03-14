package winzinger.poc.tef.wicket;

import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import senacor.hd.GreeterService;

public class HelloOSGi extends WebPage {
	@SpringBean(name="greeterServiceOsgi")
	private GreeterService greeterService;
	
	public HelloOSGi() {
		final IModel<String> greetingModel = new Model<String>("Hallo, Welt!");
		final IModel<String> nameModel = new Model<String>("Welt");
		
		Label greetingLabel = new Label("greeting", greetingModel) {
			protected void onBeforeRender() {
				super.onBeforeRender();
				greetingModel.setObject(greeterService.sayHello(nameModel.getObject()));
			};
		};
		greetingLabel.add(new AjaxSelfUpdatingTimerBehavior(Duration.milliseconds(500L)));
		add(greetingLabel);
		
		Form myForm = new Form("myForm") {
			protected void onSubmit() {
				super.onSubmit();
			};
		};
		add(myForm); 
		
		TextField<String> nameField = new TextField<String>("nameField", nameModel); 
		myForm.add(nameField);
	}
}
