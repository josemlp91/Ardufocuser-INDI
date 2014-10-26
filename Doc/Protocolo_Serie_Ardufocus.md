#Protocolo de comunicación serie Ardufocus
_____________


Nos permitirá comunicarnos con nuestro dispositivo Ardufocus, por su puerto serie, mandando comandos concretos, para que ejecute distintas acciones o nos proporcione información de su estado interno.

El uso de este protocolo debe ser compatible con el modo manual.

##Introducción.

Para crear el repertorio de comandos, lo primero que nos vamos a fijar va ha ser en los comandos de **Robofocus**, que utiliza Optec TCF

###Formato
```FX?NNNNNNZ```: 

- F -> primer carácter comando.
- X -> comando específico.
- ? -> separador comando y parámetro.
- NNNNNN -> parámetro, completando con ceros si es necesario.
- Z -> bytes menos significativo del checksum de todo la anterior.


Sin embargo en el siguiente [enlace](https://sites.google.com/site/arduinofocus/optec-tcf-protocol), se puede ver que el protocolo de Robofocus usa comando no documentados. 

Por tanto sugiere utilizar otro protocolo, con un formato similar.

###Defino un repertorio de comando basado en el de Robofocus.

Utiliza una longitud solo de 6 caracteres y no incorpora checksum, aunque se puede incorporar sin problema.


| Comando     | Uso | Descripción | Detalles |
| ----------- | ---------- | ---------- | ---------- |
| AINIT?_____ |AINIT?00000| Iniciamos modo ardufocus (ignora robofocus)
| AMODE?x____ |AMODE?M0000 AMODE?A0000 AMODE?H0000| Ajusta el modo: x=M, Modo manual, no acepta ningún comando remoto excepto AMODE 
                                            x=R  Modo remoto, no se puede manejar manualmente  
                                            x=H  Modo hibrido | 

| AG?+nnnnnnn |  GOTO ir a posicion fija
| APOSITION?_ | Devuelve la posicion actual: AP?+nnnnnnn Este comando se puede mandar en cualquier momento

| ATEMP?_____ |    | Lee la temperatura actual ATEMP?nnnn_  de nnnn 0 a 1023    |
| ALTEMP?____ |    | Lee la temperatura de la ultima vez que se enfocó: ALTEMP?nnnn |
| AMICRO?n___ | Ajusta los pasos a 1,2,4,8
| AFINE?nnn__ | Ajusta la variable de paso fino
| ASPEED?nnnn | Ajusta la velocidad del motor (pasos por segundo)
| AACC?nnnn__ | Ajusta la aceleracion
| AR?+nnnnnnn | Reseteamos la posición a +nnnnnnn (sin movimiento)

| AHLIMIT?____ | Devuelve AHLIMIT?I___ se se ha llegado al limite dentro (hardware), AHLIMIT?O___ si limite fuera o AHLIMIT?____ Si no estamos en un limite. Esos comandos pueden ser mandados en cualquier momento si se llega a un tope.

ASLIMIT?____  consulta limete software
ASILIMIT?____ ajusa limite software, inware
ASOLIMIT?____ ajusta limite software outware


| AVERS?_____ | Devuelve la version_ AVERS?nn.nnn |
| AMOV?______ | Devuelve si se está moviendo el motor: AMOV?Y_____ o AMOV?N_____ estos comandos se pueden mandar sin que se hayan pedido





Nuestra lista de comandos a atender:

Del robofocus:

FVXXXXXXZ Version















