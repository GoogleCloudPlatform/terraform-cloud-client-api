# Application Templates

Templates in this directory are automatically copied to each implementation, taking care to edit to language template specifics. 

## Templating syntax

References: 

 * <https://handlebarsjs.com/guide/expressions.html>
 * <https://jinja.palletsprojects.com/en/3.1.x/templates>
 * <https://www.thymeleaf.org/doc/tutorials/3.1/usingthymeleaf.html#standard-expression-syntax>


| Type               | Generic        | Python (Jinja) | NodeJS (Handlebars) | Java (Thymeleaf)   |
|--------------------|----------------|----------------|---------------------|--------------------|
| Variable           | `##VALUE(x)##` | `{{x}}`        | `{{x}}`             | `${x}`             |
| Conditional - if   | `##IF(x)##`    | `{% if x %}`   | `{{# if x }}`       | `th:if="${x}"`     |
| Conditional - else | `##ELSE`       | `{% else %}`   | `{{else}}`          | `th:unless="${x}"` |
| Conditional - end  | `##ENDIF`      | `{% endif %}`  | `{{/if}}`           | ``                 |
