
#define DEBUG 0

#include <TimerOne.h>
#include <LiquidCrystal.h>
#include <AccelStepper.h>
#include "Ardufocus_config.h"
#include "Ardufocus_cmd.h"

AccelStepper motor(1, PINSTEP, PINDIR);

LiquidCrystal lcd(PINLCD_RS, PINLCD_ENABLE, PINLCD_D4, PINLCD_D5, PINLCD_D6, PINLCD_D7);

int button=btnNONE;  

bool LimitRunActiveA=false;
bool LimitRunActiveB=false;

bool LimitRunSoftwareActiveA=false;
bool LimitRunSoftwareActiveB=false;

int  LimitRunSoftwareA=300;
int  LimitRunSoftwareB=-300;

int Speed = 0;
int StepPerPulse = 0;
int Position;

int temp=0;         //Temperature reading
int conT=0;         //Counter samples temperature

float temp_average=0; 
int temp_perform;   //Temperature in the moment of focus change.

int LastTimeUpdate=1000;

int microStep;
bool motorIsRun;


int lastPulse=btnNONE;
int lastSpeed = 0;
int lastStepPerPulse = 0;
int lastPosition;
int lastTemp[10];
//WorkMode mode;

              
const int bSize = 20;
const int lenghtCommand=10;
const int lenghtData=15;

int ByteCount;
 
char Buffer[bSize];             // Buffer
char Command[bSize];    // almacenamos comando
char Data[bSize];          // almacenamos parametro

bool hadToReadSpeed=true;
bool hadToReadStepPerPulse=true;

int read_LCD_buttons(int adc_key){

//Hacer que estos valores se configuren con una rutina ...

 if (adc_key > 1000) return btnNONE; 
 if (adc_key < 50)   return btnRIGHT;  
 if (adc_key < 250)  return btnUP; 
 if (adc_key < 450)  return btnDOWN; 
 if (adc_key < 650)  return btnLEFT; 
 if (adc_key < 850)  return btnSELECT;  
 return btnNONE;  

}

void welcome(String msgGreet){

  lcd.begin(16, 2);              
  lcd.setCursor(0,0);
  lcd.print(msgGreet);
  delay(3000);
  lcd.setCursor(0,0);
  lcd.print("                ");
  
}

void SetBrightness(int bright){ analogWrite(PINBRIGHTNESS,bright); }  //Brillo del LCD  

void setup() {
  LimitRunSoftwareA=-300;
  LimitRunSoftwareB=300;
  
  Serial.begin(9600);

  //Configuracion pines entrada salida.
  pinMode(PINLED_WARNING, OUTPUT);



  motor.setMaxSpeed(200);
  motor.setAcceleration(1000);

  //Variables auxiliares para  saber cuando actualizar la LCD
  LastTimeUpdate=millis();
  lastSpeed=Speed;
  lastStepPerPulse = StepPerPulse;
  lastPosition= Position;
  temp_average=analogRead(PIN_TEMSENSOR)* 0.48828125;

  //Interrupciones software y hardware.
  Timer1.initialize(50);
  Timer1.attachInterrupt( timerFunction );                                         
//         attachInterrupt ( 0, finA,RISING);   
//         attachInterrupt ( 1, finB,RISING);

  //Ajustamos el brillo y saludo inicial.
   SetBrightness(50);
   welcome("  ARDUFOCUSER"); 

}


void finA(){ LimitRunActiveA=true; }
void finB(){ LimitRunActiveB=true; }

void timerFunction() { 

  if (!LimitRunActiveA and !LimitRunActiveB){ 
  
      // if (Position >= LimitRunSoftwareA ){
      //   LimitRunSoftwareActiveA=true;
      //   if (motor.targetPosition <= LimitRunSoftwareA)
      //     motor.stop();
      //   else motor.run(); LimitRunSoftwareActiveA=false;
      // }

      // if (Position <= LimitRunSoftwareB ) {
      //   LimitRunSoftwareActiveB=true;
      //   if (motor.targetPosition() >= LimitRunSoftwareB)
      //     motor.stop();
      //   else motor.run(); LimitRunSoftwareActiveB=false;
      // }

      motor.run();
   }  ///Necesita testearse muy bien

  else if (LimitRunActiveA){

    if (motor.targetPosition() < motor.currentPosition()){
      motor.run(); 
      LimitRunActiveA=false;

    } else { motor.stop(); LimitRunActiveB=true; }
  
  }

  else if (LimitRunActiveB){

    if (motor.targetPosition() > motor.currentPosition()){
      motor.run();
      LimitRunActiveB=false;

    } else { motor.stop(); LimitRunActiveB=true; }

  }
}

long lastTimeReadButton = 0;
long lastTimeReadController = 0;

void ReadManualController(){

if (millis() > lastTimeReadButton + 5) {
  int adc_key = analogRead(PIN_BUTTON);
  button = read_LCD_buttons(adc_key);
  lastTimeReadButton = millis();
}

else button=lastPulse;

if (millis() > lastTimeReadController + 200) {


   int s = analogRead(PIN_POTA);
   if (s==0) hadToReadSpeed=true;

   if (hadToReadSpeed){
      Speed = map(s, 0, 1024, MINVEL, MAXVEL ); 
   }

   int sS = analogRead(PIN_POTB);
   if (sS==0) hadToReadStepPerPulse=true; 

   if (hadToReadStepPerPulse){  
      StepPerPulse = map(sS, 0, 1024, 1, MAXSTEPPXPULSA);
   }  

   if (abs(motor.speed() - Speed ) > 2) {
     motor.setMaxSpeed(Speed);
   }

   lastTimeReadController = millis();
  }
}


void ReadSensor(){
 // Other Sensor
 //LimitRunActiveA=digitalRead(PIN_LIMITRUNA);
 //LimitRunActiveB=digitalRead(PIN_LIMITRUNB);
 //LimitRunActiveA=LimitRunActiveB=false;
  
}


long lastTimeReadTemp = 0;
float ReadTemperature(){
 
  if (millis() > lastTimeReadTemp + 10000) {

      float t;
      t=analogRead(PIN_TEMSENSOR)* 0.48828125;
      if (abs(t-temp_average)<=2) temp_average=t;

      lastTimeReadTemp = millis();
    }

  return temp_average;

}


// long lastTimeReadTemp = 0;
// void ReadTemperature(){

//   if (millis() > lastTimeReadTemp + 100) {

//     lastTemp[conT]= analogRead(PIN_TEMSENSOR) ;
//     conT++;
//     if (conT==10){
//       Serial.println(conT);
//       Serial.println(temp_average);
//       for (int i = 0; i<10; ++i){
//         temp_average+=lastTemp[i];
        

//       }
//       temp_average/=10;
//       temp_average*=0.48828125;
//       conT=0;
//     }

//     lastTimeReadTemp = millis();
//   }

// }


long LastTimeUpdateLcd = 0;

void UpdateLCD(){

  if (millis() > LastTimeUpdateLcd + 500) {

    if (button != lastPulse)  {

      lcd.setCursor(0,1);
      switch (button){
        case btnRIGHT:  lcd.print("RIGHT  ");  break;
        case btnLEFT:   lcd.print("LEFT   ");  break;
        case btnUP:     lcd.print("RIGHT_F");  break;
        case btnDOWN:   lcd.print("LEFT_F ");  break;
        case btnSELECT: lcd.print("OK     ");  break;
    
      lastPulse=button;

      }

    }

      ///No actualizar pantalla por diferencia, sino por tiempo.!!!!

       lcd.setCursor(2,0);
       lcd.print("    ");
       lcd.setCursor(2,0);
       lcd.print((int)temp_average);
        
       if ((lastSpeed>Speed+5) or (lastSpeed<Speed-5))  {
         lcd.setCursor(8,1);
         lcd.print("    ");
         lcd.setCursor(8,1);
         lcd.print(Speed);
         lastSpeed=Speed;
       }
      
       if ((lastStepPerPulse>StepPerPulse+5) or (lastStepPerPulse<StepPerPulse-5))  {
         lcd.setCursor(12,1);
         lcd.print("    ");
         lcd.setCursor(12,1);
         lcd.print(StepPerPulse);
         lastStepPerPulse=StepPerPulse;
      }  
         
       if ((lastPosition>Position+1) or (lastPosition<Position-1)){
         lcd.setCursor(4,0);
         lcd.print("            ");    
         lcd.setCursor(8,0);
         lcd.print(Position);
         lastPosition=Position;
     }

     LastTimeUpdateLcd = millis();
  }      

}


bool ChangeThermalOptical(int temp_average, int temp_perform){
   if ((temp_average>temp_perform+MAX_CHANGE_TEMP) || (temp_average<temp_perform-MAX_CHANGE_TEMP)) return true;
   else return false;
}

void enable_warning() { digitalWrite(PINLED_WARNING,HIGH); }
void disable_warning(){ digitalWrite(PINLED_WARNING,LOW); }

void ManualPerformance(){

  //if (mode!=REMOTE){
  //motor.setAcceleration(DEF_ACC);   // Esta funcion no se puede ejecutar mucho
  Position=motor.currentPosition();

  if (ChangeThermalOptical(temp_average, temp_perform)) enable_warning();
      
  switch (button){

    case btnNONE: {

                    if ((lastPulse==btnLEFT)||(lastPulse==btnRIGHT)){  
                      motor.stop(); 
                    }
                   lastPulse=btnNONE; 
                   break;
      
    }

    case btnRIGHT: {
                    motor.moveTo(motor.currentPosition()-3000);
                    lastPulse=btnRIGHT; 
                    break;
    }

    case btnLEFT:   {
                      motor.moveTo(motor.currentPosition()+3000); 
                      lastPulse=btnLEFT; 
                      break;
    }

    case btnUP:   {
                    if (lastPulse!=btnUP){
                        motor.moveTo(motor.currentPosition() + StepPerPulse);
                    }
                    
                    lastPulse=btnUP;    
                    break;
    }

    case btnDOWN: {
                    if (lastPulse!=btnDOWN){
                        motor.moveTo(motor.currentPosition() - StepPerPulse);
                    }
                    
                    lastPulse=btnDOWN;  
                    break;
    }

    case btnSELECT: {
                      temp_perform=temp_average;
                      disable_warning();
                      motor.setCurrentPosition(0);     
                      break;
    }

  } 

  button=btnNONE;
}


void SerialParser(char delimiter) {

 if (Serial.available()){

   ByteCount = -1;

   for (int x=0;x<bSize;x++){
      while(!Serial.available()) {}

      Buffer[x]=Serial.read();
   }

   ByteCount =  20; 
   
  }

   if (ByteCount  > 0) {

        char c=Buffer[0];          
        int i=0;

        for (i; ((i<ByteCount) and (c!= delimiter)) ; i++){
              Command[i]=c;
              c=Buffer[i+1];
        }
                
        i++;
        int a=0;
          for (i; (i<ByteCount) ; i++){
            c=Buffer[i];
            Data[a]=c;
            a++;
        }                  
     }


   memset(Buffer, 0, bSize);  

}

void ConfirmPositionGet(int pos){     }
void RemoteManager(){

//if (mode!=MANUAL){ 
  char  d='?';
  SerialParser(d);

  if (ByteCount  > 0) {
    
    if      (Command[0]=='F'){ /*Implementar*/ }      
    else if (Command[0]=='A'){  DriveCommandArdufocus(); }
    else    Serial.println("No comando");
    
  if (DEBUG){
    Serial.print("CMD      : ");
    Serial.println(Command);
    Serial.print("PAR      : ");
    Serial.println(Data);
    Serial.flush();                      
  }       
}

memset(Command, 0, sizeof(Command));
memset(Data, 0, sizeof(Data));
ByteCount=0;                         
      
//}

 }


void sendMessageToIndi(String message){

  int len=message.length();
  for (int i=len;i<bSize;i++){
    message+=' ';
  }

  Serial.println(message);
  Serial.flush();

} 

void DriveCommandArdufocus(){

  if      (!(strcmp(Command,allcmdArdu[AINIT])))  { Serial.println("Modo ardufocus"); }
  else if (!(strcmp (Command,allcmdArdu[AMODE]))) { Serial.println("Ajustando modo"); }
  
  else if (!(strcmp (Command,allcmdArdu[AG])))  {      
    
    int Ipar=atoi(Data);
    motor.moveTo(Ipar); 
    if (DEBUG) { Serial.println(motor.currentPosition());  } 
       
  }
  
  else if (!(strcmp (Command,allcmdArdu[APOSITION])))  {
    
    String posA(motor.currentPosition());
    sendMessageToIndi("APOSITION?"+posA);
    
  }
   
  else if (!(strcmp (Command,allcmdArdu[ATEMP])))  { 
    
    String atemp((int)temp_average);
    sendMessageToIndi("ATEMP?"+atemp);       
 
  }

  else if (!(strcmp (Command,allcmdArdu[ALTEMP])))  { 
    
    String atemp(temp_perform);
    sendMessageToIndi("ALTEMP?"+atemp); 
 
    
  }
  
  else if (!(strcmp (Command,allcmdArdu[AMICRO])))  { Serial.println("Ajusta micropasos"); }
  else if (!(strcmp (Command,allcmdArdu[AFINE])))   { 

    int Ipf=atoi(Data);
    if (Ipf>MAXSTEPPXPULSA) Ipf=MAXSTEPPXPULSA;
    if (Ipf<1) Ipf=1;

    StepPerPulse=Ipf;
    String pf(Ipf);
    hadToReadStepPerPulse=false;
    sendMessageToIndi("AFINE?"+pf);
    

  }

  else if (!(strcmp (Command,allcmdArdu[ASPEED])))  {
     
     int Ivel=atoi(Data);
     if (Ivel>MAXVEL) Ivel=MAXVEL;
     if (Ivel<MINVEL) Ivel=MINVEL;
     Speed=Ivel;
     String vel(Ivel);
     hadToReadSpeed=false;
     sendMessageToIndi("ASPEED?"+vel);
     
  }
 
  else if (!(strcmp (Command,allcmdArdu[AACC])))  { 
     
     int Iacc=atoi(Data);
     if (Iacc>MAXACC) Iacc=MAXACC;
     if (Iacc<MINACC) Iacc=MINACC;
     
     String acc(Iacc);  
     motor.setAcceleration(Iacc);
     sendMessageToIndi("AACC?"+acc);
              
  }
  
  else if (!(strcmp (Command,allcmdArdu[AR]))) { 
    
      int Ir=atoi(Data);
      String r(Ir); 
      motor.setCurrentPosition(Ir);
      sendMessageToIndi("AR?"+r);
       
  }

  else if (!(strcmp (Command,allcmdArdu[AHLIMIT]))) { 
    
    if (LimitRunActiveA)sendMessageToIndi("AHLIMIT?I");
    else if (LimitRunSoftwareB) sendMessageToIndi("AHLIMIT?O"); 
    else sendMessageToIndi("AHLIMIT?"); 
  
  }

  //pendiente de implementar.
  else if (!(strcmp (Command,allcmdArdu[ASLIMIT] ))) { Serial.println("Consulta limite software"); }
  else if (!(strcmp (Command,allcmdArdu[ASILIMIT]))) { Serial.println("Ajusta limite software inware"); }
  else if (!(strcmp (Command,allcmdArdu[ASOLIMIT]))) { Serial.println("Ajusta limite software outware"); }

  else if (!(strcmp (Command,allcmdArdu[AVERS]))) { sendMessageToIndi("AVERS?1"); }
  else if (!(strcmp (Command,allcmdArdu[AMOV]))) {  

    if (motor.distanceToGo() == 0) {
    sendMessageToIndi("ASTOP?");    
    } 

  }
  
  //Comandos debug 
  else if (!(strcmp (Command,allcmdArdu[ARUNA])) && DEBUG) {
    motor.moveTo(30000);
  }
  
  else if (!(strcmp (Command,allcmdArdu[ARUNB])) && DEBUG) {
    motor.moveTo(-30000);
  }
  
  else if (!(strcmp (Command,allcmdArdu[ASTOP])) ) {
    motor.stop();
  }
  
}

long lastCurrentPos= millis();

void SendCurrentPosition(){

  if (millis()> lastCurrentPos + 1000){
    
    String r(motor.currentPosition());
    sendMessageToIndi("APOSITION?"+ r); 
  
  if (motor.distanceToGo() == 0) {
    sendMessageToIndi("ASTOP?");    
  }

  lastCurrentPos=millis();
  }

}

void loop(){

  ReadManualController();
  ReadSensor();
  ReadTemperature();
  UpdateLCD();
  ManualPerformance();
  SendCurrentPosition();  //cada segundo mandar el currentPosition()
  RemoteManager(); 

}
 

