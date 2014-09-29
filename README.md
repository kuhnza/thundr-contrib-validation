thundr-contrib-validation
=========================

[JSR-303](http://beanvalidation.org/1.0/spec/) bean validation support for [thundr](http://3wks.github.io/thundr/).

The hibernate-validation implementation is used under the covers which provides all the standard JSR annotations
plus a few really handy ones such as `@NotBlank`.

[![Build Status](https://travis-ci.org/kuhnza/thundr-contrib-validation.png)](https://travis-ci.org/kuhnza/thundr-contrib-validation)

Usage
-----

1. Declare the dependency in your POM:
```xml
<dependency>
	<groupId>com.threewks.thundr</groupId>
	<artifactId>thundr-contrib-validation</artifactId>
	<version>1.0.0</version>
	<scope>compile</scope>
</dependency>
```

2. Register the module with the dependency registry:
```java
public class ApplicationModule extends BaseModule {

	@Override
	public void requires(DependencyRegistry dependencyRegistry) {
		super.requires(dependencyRegistry);
		dependencyRegistry.addDependency(ValidationModule.class);
		
		// ... other dependencies
	}
}
```

3. Inject away!
```java
public class MyController {
	private final Validator validator;
	
	public MyController(Validator validator) {
		this.validator = validator;
	}

	public JspView myView(MyModel model) {
		Set<ConstraintViolation<MyModel>> validationErrors = validator.validate(model);
		
		Map<String, Object> viewModel = new HashMap<>();
		viewModel.put("validationErrors", validationErrors);
		return new JspView("myview", viewModel);
	}
}
```

Bonus
-----

I just love freebies, don't you? 

You might have noticed that `Set<ConstraintViolation<T>>` doesn't serialise particularly well. That makes it
pretty bad for doing AJAX validation. Don't worry though I've created this handy little class: `ConstraintViolationSetToMapTransformer` 
for just such an occasion.

You use it like so:
 
```java
ConstraintViolationSetToMapTransformer<MyModel> toMap = new ConstraintViolationSetToMapTransformer<>();
Set<ConstraintViolation<MyModel>> validationErrors = validator.validate(model);
Map<String, Object> errorMap = toMap.from(validationErrors);
```

This produces a nice little graph of maps keyed by the field names of the beans with the values set to error messages. You 
can then check for errors in your JavaScript like this:

```javascript
// POST some data to the server that will get validated
$.post('/validate', myFormData)
	.success(function(data) { // do something successy })
	.error(function(jqxhr) { 
		var errors = JSON.parse(jqxhr.responseText);
		
		if (errors.someProperty) {
			console.log("Validation error: " + error.someProperty);
		}
		
		if (errors.nestedObject && errors.nestedObject.nestedProperty) {
			console.log("Nested field validation error: " + errors.nestedObject.nestedProperty);
		}
	});
```



