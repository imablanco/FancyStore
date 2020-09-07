# FancyStore

## Project structure

The project is structured as a multimodule project where every architecture layer is contained in a separated module. Althogh there is a presentation layer, it only contains base classes, ViewModel definitions are inside app module to provide a better feature class grouping.

#### Domain

Contains repository definitions, use cases, domain models and discounts business rules.  
All the repository responses are modeled to return `Either` instances (functional data type to to provide pure error handling), easing the way data flows from layers and making the app less error prone by providing a common model to wrap computations.

#### Data

Contains domain repository implementations and data sources.  
Network source uses `Retrofit` with a custom `CallAdapter` that transforms network responses to `Either` instances.  
Cache source uses a custom in memory cache called `MemoryCache`(basically a `Map` with some intelligence regarding cache absence). There is also a reactive cache implementation that uses `Flow` called `FlowMemoryCache`(backed by a `MemoryCache`).  
I have choosen this memory-like cache because using disk cache like SQL or some other disk cache based solutions seemed like an overkill for the challenge purpose and I think the main challenge here is how interaction between data sources and data gathering strategy is done.

Is also worth to mention that every data source returns `Either` instances. This way we can ensure that data is always safe from unexpected errors from the origin.

#### Presentation

To model presentation layer I choosed a kind of MVI pattern that really consists on a VM with some MVI influences. By using Android's `ViewModel`class we can take advantage of all the framework integrations made around it (many of the new APIs are evolving towards `LifecycleOwner` so I think is worth going in the same direction). Also by putting MVI concepts like a single class representing View state and driving user actions as unified Intents makes the data flow so much clearer and provides a simpler app state management.

Depending of the app I change some aspects of how VM exposed types are grouped or modeled but at least I always include:  
* `ViewState` class that represents the state of the screen
* `ViewAction` class that represents one shot actions (like showing a Snackbar or navigating to another screen)
* `Intent` class that represents the actions that the user send to the VM

If the app for example has a heavy navigation I tend to split between ViewActions and NavigationActions, but it is just a conceptual grouping.

#### DI

DI is done through Koin. Although is not a real DI framework (but a very well designed dependency container) it does the work really well for small apps and its faster that setting up Dagger.  
Also build times are something to keep in mind and in the last releases they also improved how definitions are resolved and indexed so in my opinion every day they get closer to being a real alternative to Dagger.

#### Testing

I always do testing (Unit and instrumented) by providing test fakes or mocks for repository or use cases assuming that most of the logic work load is done by the ViewModel.  Also by ensuring fakes in UI testing we can get rid of `IdlingResource`s by making all the data flow sync.
Although I played around with some testing frameworks like Spek I feel more confortable going with plain assertions, only using `Mockito` to mock use cases responses. 

### Domain business rules

One of the most important parts of the app is how discounts are computed. (Although in a real app this discounts should be calculated in back).  

Discounts are modeled trough `Discount` sealed class that currently has two inheritors (one by type of known discount)  
* FreeItemDiscount
* BulkDiscount

`FreeItemDiscount` represents MxN discounts where buying M products the user gets N for free.
`BulkDiscount` represents X% off discounts where buying N or more products of a type applies X% off on all of them.

To provide an scalable way of creating and applying different discounts I constructed some classes that work together:

#### DiscountValidator

Its generic on `Discount` and has the responsability of validating if the given `Discount` is valid for the received products.


