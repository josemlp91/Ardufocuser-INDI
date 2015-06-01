# Adquisicion


Consiste básicamente en obtener imagenes mediante el control de un sensor CCD, obteneindo imágenes en formato FITS, incluyendo tambien los metadatos correspondientes en el head.

Por citar algunos de los metadatos más interesantes que podemos encontrar en el head de una imagen FITS, nos encontramos:

**JUL-DATE:** El dia-hota de la exposición en fomrato de Fecha Juliana.

**RA, DEC (Ascensión Recta y Declinación):** Las coordenadas ecuatoriales del centro de la imagen. RA, o α, es el ángulo
del objeto proyectado sobre el ecuador celeste desde el punto Aries (equivalente a la longitud geográfica) y DEC, δ, es el ángulo entre el ecuador celeste y el objeto (equivalente a la latidud geográfica).

**CD:** Coordinate Description matrix. La matriz que define la rotacion y escala respecto a las coordenadas ecuatoriales.

**AIRMASS:** La masa aire/atmosfera que existe entre el telescopio y el objeto observado.

De media una imagen del cielo puede contener un par de cientos de estrellas, dependiendo de la región del cielo observada.

###Las estrellas

El perfil en la imagen de cada estrella se puede aproximar con una curva
Gaussiana 2D).
El perfil unidimensional de esta gaussiana se caracteriza en astronomia por medio de su ancho a la mitad del valor máximo, que es el FWHM (Full Width at Half Maximum), que depende del proceso dispersivo en la atmosfera y por lo tanto es aproximadamente constante para todas las fuentes puntuales de la imagen.

Tambíen se producen detecciones esp ureas causadas por rayos  osmicos
que aleatoriamente llegan al sensor y que provocan picos de intensidad en la imagen,
o píxeles defectuosos del sensor.

![](https://raw.githubusercontent.com/josemlp91/Ardufocuser-INDI/master/Doc/book/images/estrella_brillante.png)


