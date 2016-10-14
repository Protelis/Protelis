# How to write tests
Tests are parsed as normal Protelis programs, with an exception. There must be also a result included in a leading comment. Rules:
 * The result must be enclosed in a leading comment (C-99 form /\* comment \*/ is preferred), possibly multiline.
 * The result must be a valid a Protelis program.
 * The result must be written after a "EXPECTED_RESULT:" string.
 * There is a very, very trivial Regexp matching such string. Therefore, if you use this very same string before the result declaration, you are calling for problems
 * If your program results depend upon the number of cycles (e.g. because there is a ```rep``` in your program), you can use the syntax ```$CYCLE```, that will be syntactically substituted (with a macro) with the number of cycles that will be run in total.
 * If your program needs a different testing, which involve Java, e.g. it must check the types by means of ```instanceof```, there are facilites to do so in ```TestLanguage.java```. In this case, you can omit the result string.
 
### Examples
##### Simple example
The program must return 1
```
/* 
 * EXPECTED_RESULT: 1
 */
```

##### More elaborate example
The program must return a tuple of tuples
```
/* 
 * EXPECTED_RESULT: [[1, 2][3, 4][4, 5]]
 */
```

##### Example with operations
The program must return a tuple of tuples, whose last value depends on the number of runs executed:
```
/* 
 * EXPECTED_RESULT: [[1, $CYCLE + 2]] 
 */
```
