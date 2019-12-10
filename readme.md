## Installation
1. Add jitpack repository to your project path  
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' } 
    }
}
```  
  
2. Add the dependency  
```groovy
dependencies {
    implementation 'com.github.intelligentpayments:androidSDK:0.4.0'
}
```  
  
## Usage  
  
1. To use Android SDK it's necessary to fetch cashier url and token from API:  
2. Then to display web page via SDK, call this method from your activity:  
```kotlin
class YourActivity: Activity() {
    fun startPayment() {
        startEvoPaymentActivityForResult(
        	requestCode, 
	        merchantId, 
	        cashierUrl, 
	        token, 
	        myriadFlowId
        )
    }
}
```  
or if you use Java:  
```java
class YouActivity extends Activity() {
    private void startPayment() {
        EvoPaymentActivityKt.startEvoPaymentActivityForResult(
            this, //Activity
            requestCode,
            merchantId,
            cashierUrl,
            token,
            myriadFlowId
        );
    }
}
```  
3. Get result:  
```kotlin  
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data) 
    if (requestCode == EVO_PAYMENT_REQUEST_CODE) {
        when (resultCode) {
            EvoPaymentActivity.PAYMENT_SUCCESSFUL      -> onPaymentSuccessful()
            EvoPaymentActivity.PAYMENT_CANCELED        -> onPaymentCancelled()
            EvoPaymentActivity.PAYMENT_FAILED          -> onPaymentFailed()
            EvoPaymentActivity.PAYMENT_UNDETERMINED    -> onPaymentUndetermined()
            EvoPaymentActivity.PAYMENT_SESSION_EXPIRED -> onSessionExpired()
        }
    }
}
```