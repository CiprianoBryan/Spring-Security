# SECURITY

## Authentication (OAuth)

* Is the process of letting an application know who we are.
* First step is log in by providing our user ID and password.
* If the authentication is successful, then we can access other pages in the application. If not, we cannot.
* There are different ways to authentication in the HTTP or web application world, starting with Basic auth, Form based auth, and then in the restful API world we can implement OAuth single sign on where we implement authentication and authorization for all applications within our organizations through single sign on.
* We can also create our own custom login mechanism instead of using basic Form or OAuth, we can create our own authentication and authorization process as well.

## Authorization (OAuth)

* Once a user logs in or authenticate, how does the application know how much access the user or another applications has?
* That is where authorization jumps in and it uses roles to do authorization.
* Each role is mapped to certain URLs or methods in the application, and the user with certain roles will have access to certain functionality within the application and certain users will not have access to certain functionality in the application.

## Confidentiality (Encryption and Decryption)

* This is where applications ensure that the data they are sharing is not vulnerable to hackers.
* That is where encryption and decryption comes in.
* The simplest way of encrypting and decrypting is by using his HTTPS.
* Once the communication is encrypted, the data that is exchanged, even if the hacker captures it, he will not be able to make sense out of it because these application the sender application will use a certain PUBLIC KEY and sends that user details. And then the application will use a PRIVATE KEY which will decrypt those details. Even if a hacker gets the details in between, he will not be able to do anything with those details unless he has the private key.

## Integrity (Signatures)

* Integrity is where applications can ensure that whatever data is being exchanged is really coming from the same user that is expecting or the application it is expecting and it is not changed in the process.
* For example, if this hacker captures the authentication details or any data, if he put something else in the data and sends it to the banking application, the banking application should know that the data was not tweaked and the integrity is still maintained.

## AuthorizationServer and ResourceServer

* When you work with OAuth and JWT, you will lean about authorization-server and resource-server.
* When the authorization-server creates a token and gives it back to the application, that application will send that token to the resource-server.
* How does the resource-server ensure that this token is really produced by that authentication-server? By using signatures, the authentication token will use a PRIVATE-KEY again and it will sign this token and only the applications with the PUBLIC-KEY corresponding to the PRIVATE-KEY will be able to verify the signature.
* If a hacker captures that token in between, if he tweaks it, if he changes the token when the token is received by the resource-server, it will calculate the signature. On that token, the signature sent by the authorization-server, along with the token, should match the signature that this resource-server calculates. If it doesn't match, it knows that somebody has hacked it and the integrity is not maintained anywhere.
* So integrity is checked using signatures.

## Cross Site Request Forging (CSRF) and Cross Origin Resource Sharing (CORS)

* CSRF will prevent an other website to submit data on our behalf
* CORS will allow applications running, especially if you are developing your frontend using Angular or React, backend using Java or Node for these applications which are running on different domains need to communicate with each other without any issues.
* Browsers will not allow CORS by default. It should enable CORS within your server side applications

## Key Security components

When a End-user sends a request or a Restful-client sends the request to our Rest-application.
* The very first component in Spring Security that intercepts that requests is the AUTHENTICATION-FILTER. The authentication filter is a servlet filer class that will see if the user has authenticated. If not, it will send that request to the AUTHENTICATION-MANAGER to check if the details sent by the user are correct. If the username and password are valid, the authentication manager in turn uses authentication, provided this is where the login logic or the authentication logic is defined.
* The AUTHENTICATION-PROVIDER will not fetch the user details from the database or from in memory. It will use USER-DETAILS-SERVICE for that purpose. It also uses a PASSWORD-ENCODER because we don't want to store passwords in plain text so the passwords will be encoded, the incoming password from the user will be encoded and then the comparison is done.
* Once the AUTHENTICATION-PROVIDER checks, if the authentication details, the username, password, etc are correct, then it will send the appropriate response back to the AUTHENTICATION-MANAGER.
* The AUTHENTICATION-MANAGER hands it back to the AUTHENTICATION-FILTER if the user details are OK, if the authentication succeeded.
* The AUTHENTICATION-FILTER will use a AUTHENTICATION-SUCCESS-HANDLER and stores that authentication information the user entity itself in a SECURITY-CONTEXT.
* In an instance of security context, if the authentication failed, it will use the AUTHENTICATION-FAILURE-HANDLER to send the appropiate response back to the client

### NOTES:

* AUTHENTICATION-PROVIDER: This is where the login logic is defined
* USER-DETAILS-SERVICE is responsible for fetching the user information. It could be form LDap, it could be from an authorization server of OAuth, it could be a database, in-memory, etc.
* PASSWORD-ENCODER is responsible for encoding the passwords.
* SECURITY-CONTEXT is where the user information is stored for future.

![image](https://user-images.githubusercontent.com/36827327/194742123-47d31c19-0ab7-43c9-9b06-0099c6bef383.png)

-----------------------------------------------------------------------------------------

# AUTENTICACI??N B??SICA

1. Creamos el proyecto con Spring Initializr con las dependencias Lombok, Spring Web y Spring Security
* Al agregar la dependencia Spring Security autom??ticamente agregar?? la Autenticaci??n b??sica, cada vez que se ejecute la aplicaci??n se crear?? un usuario con username: user y un password UUID (Universal Unique ID), con el cual servir?? para autenticarnos y poder utilizar los endpoints.  
2. Cuando se autentica correctamente, se guardar?? la informaci??n de usuario y el login correcto dentro del SECURITY-CONTEXT, este generar?? una COOKIE llamada J-Session-ID. Esta COOKIE se enviar?? devuelta y se almacenar?? en Postman o alg??n Navegador web en headers, si llamas a esa URL ellos enviar??n la COOKIE devuelta cada vez.
3. As?? que junto al request, la COOKIE tambi??n ir?? al filtro, ver?? si hay un log correspondiente en el SECURITY-CONTEXT. Si est??, no solicitar?? el inicio de sesi??n.
4. Si intentamos loguearnos nuevamente con una constrase??a falsa, ingresar?? correctamente solo si es que el J-Session-ID se qued?? seteada del anterior login. Esto sucede en el Postman y en el navegador web.

# AUTENTICACI??N PERSONALIZADA

1. WebSecurityConfigurerAdapter: Tiene 3 diferentes m??todos de configuraci??n que podemos sobreescribir.

2. El primero toma una AuthenticationManagerBuilder: Esto nos permitir?? personalizar el AUTHENTICATION-MANAGER as?? todos esos componentes pueden ser personalizados.

3. Creamos nuestro MySecurityConfig con la anotaci??n @Configuration y que extiende de WebSecurityConfigurerAdapter
* Sobreescribimos el m??todo void:configure(AuthenticationManagerBuilder auth), y void:configure(HttpSecurity http)
* En void:configure(HttpSecurity http), colocamos http.authorizeRequests().anyRequest().authenticated() el cual significa que todas las peticiones que llegan a nuestra aplicaci??n deber??n ser autenticadas, solo entonces pueden acceder a los recursos, si adem??s agregamos .permitAll() entonces cualquiera puede acceder a nuestra aplicaci??n.
* En void:configure(AuthenticationManagerBuilder auth), creamos nuestro USER-DETAILS-SERVICE como InMemoryUserDetailsManager, de esta forma almacenaremos la informaci??n de usuario en memoria.
* Tambi??n creamos nuestro PASSWORD-ENCODER como BCryptPasswordEncoder, y a las contrase??as de los usuarios lo codificamos con el m??todo encode del objeto.
* Al AuthenticationManagerBuilder le pasamos el USER-DETAILS-SERVICE y PASSWORD-ENCODER creados anteriormente.
* Al usuario le a??adimos una autoridad (authority)
* Eliminamos el JSESSIONID y probamos el log in con el usuario/password tom/cruise.

4. Extraemos nuestro PASSWORD-ENCODER
* Creamos un @Bean passwordEncoder() que retorne el BCryptPasswordEncoder
* Inyectamos con @Autowired un PasswordEncoder, y lo usamos dentro del AuthenticationManagerBuilder
* Quitamos del userDetailsService el seteo del PASSWORD-ENCODER, probamos y funciona correctamente.
* El AUTHENTICATION-MANAGER autom??ticamente buscar?? un bean que est?? disponible y usar?? el PASSWORD-ENCODER que hace que funcione.
* ??Qu?? pasa si tu no configuras un bean, y no codificas la contrase??a? Esto retornar?? un IllegalArgumentException, ya que el PASSWORD-ENCODER es obligatorio 

5. Creamos un AUTHENTICATION-PROVIDER, implementamos el interface AuthenticationProvider el cual tiene 2 m??todos (authenticate, suppots)
* The Authentication tiene el username y password
* retornamos como Authentication la clase UsernamePasswordAuthenticationToken con los par??metros userName, password, y la lista de autorizaciones el cu??l en este caso es vac??o.
* Si la autorizaci??n falla retornamos un BadCredentialsException
* Este AUTHENTICATION-PROVIDER responder?? un UsernamePasswordAuthenticationToken al AUTHENTICATION-MANAGER, luego ir?? al AUTHENTICATION-FILTER. Si la autenticaci??n es exitosa, almacenar?? el username y password en el SECURITY-CONTEXT. Si falla lanzar?? un BadCredentialsException y el usuario obtendr?? un 401.
* Nosotros debemos decir que cargamos el UsernamePasswordAuthenticationToken mediante el m??todo supports. As?? en tiempo de ejecuci??n, el AUTHENTICATION-MANAGER env??a este tipo de dato como autenticaci??n.
* El trabajo del AUTHENTICATION-MANAGER es ir mediante todos los PROVIDERS que estan disponibles para ayudar con el Basic Authentication o el UsernamePasswordAuthentication, cualquiera que indique que pueda hacerlo (supports return true) primero se usar??.
* No olvidar marcar la clase con la anotaci??n @Component, sino no ser?? escaneada.

5. Podemos usar el formLogin, as?? se desplegar?? un form para el log in, introducimos el usuario/contrase??a tom/cruise e ingresamos. (En navegador web)

6. Si agregamos otro endpoint, al loguearnos en uno podemos hacer uso del otro, ya que quedamos autenticados
* Cambiamos el anyRequest() por antMatchers("/hello") para indicar el endpoint que necesita Autenticaci??n para realizar la petici??n. Si lo dejamos as??, el resto de endpoints si podr??amos acceder directamente.
* Si a??adimos .anyRequest().denyAll() entonces todos los request distintos al especificado en el antMatchers() estar??n denegado su acceso (ni autentic??ndonos podr??amos ingresar a esos endpoints)

7. Crear nuestro AUTHENTICATION-FILTER (Servlet filters) y agregarlo al Spring Security Filter Chain
* Creamos la clase MySecurityFilter implements Filter (javax.servlet.*) el cual implementaremos el m??todo doFilter cuyos par??metros son ServletRequest, ServletResponse y FilterChain
* EL FilterChain tendr?? la informaci??n de todos los filtros Servlet, y lo aplicamos al request y response con chain.doFilter(request, response)
* Podemos agregar l??gica antes de aplicar el filtro (chain.doFilter()), esto se aplicar?? cuando el request est?? llegando al siguiente componente en la cadena
  * En medio de AUTHENTICATION-FILTER ----> AUTHENTICATION-MANAGER
* Podemos agregar l??gica despu??s de aplicar el filtro (chain.doFilter()), esto se aplicar?? cuando el response est?? devuelta del AuthenticationManager
  * En medio de AUTHENTICATION-FILTER <---- AUTHENTICATION-MANAGER
* Agregamos nuestro filtro al HttpSecurity, mediante el http.addFilterAfter(), 
  * colocamos nuestro filtro en el primer par??metro (new MySecurityFilter()), 
  * colocamos el BasicAuthenticationFilter.class como segundo par??metro, esto lo realizar?? despu??s del primer filtro.
  * Existen distintos filtros, como filtro basic authentication, filtro CSRF, etc. 

# Spring Security Architecture

![image](https://user-images.githubusercontent.com/36827327/194778467-b806d550-0104-4384-9eff-98365016633b.png)

[Fuente](https://www.toptal.com/spring/spring-security-tutorial)

# Spring Security JWT Architecture

![image](https://user-images.githubusercontent.com/36827327/194795420-a435c1a4-98c0-4a29-89ab-dcb9db7426e7.png)

[Fuente](https://www.bezkoder.com/spring-boot-jwt-mysql-spring-security-architecture/)