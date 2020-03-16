package test.com.linkallcloud.um.pc;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

public class TemplateConvertUtils {
	

	public static String stringTemplate(String stringTemplate, Map<String, Object> data) {
		Locale locale = Locale.getDefault();
		
		StringTemplateResolver templateResolver = new StringTemplateResolver();
		templateResolver.setCacheable(true);
		templateResolver.setCacheTTLMs(3600000L);
		templateResolver.setName("stringTemplate");
		templateResolver.setTemplateMode(TemplateMode.TEXT);
		
		TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process(stringTemplate, new Context(locale, data));
	}
	

	public static void main(String[] args) {
		String tmpl = "Dear [(${customer})],This is the list of our products:";
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("customer", "周栋");
		
		String ret = TemplateConvertUtils.stringTemplate(tmpl, data);
		System.out.println(ret);
	}

}
