# FancyStore

# Project structure

The project is structured as a multimodule project where every architecture layer is contained in a separated module. Althogh there is a presentation layer, it only contains base classes, ViewModel definitions are inside app module to provide a better feature class grouping.

### Domain

Contains repository definitions, use cases, domain models and discounts business rules.  
All the repository responses are modeled to return `Either` instances (functional data type to to provide pure error handling), easing the way data flows from layers and making the app less error prone by providing a common model to wrap computations.

### Data

Contains domain repository implementations and data sources.  
Network source uses `Retrofit` with a custom `CallAdapter` that transforms network responses to `Either` instances.  
Cache source uses a custom in memory cache called `MemoryCache`(basically a `Map` with some intelligence regarding cache absence). There is also a reactive cache implementation that uses `Flow` called `FlowMemoryCache`(backed by a `MemoryCache`).  
I have choosen this memory-like cache because using disk cache like SQL or some other disk cache based solutions seemed like an overkill for the challenge purpose and I think the main challenge here is how interaction between data sources and data gathering strategy is done.

Is also worth to mention that every data source returns `Either` instances. This way we can ensure that data is always safe from unexpected errors from the origin.

### Presentation

To model presentation layer I choosed a kind of MVI pattern that really consists on a VM with some MVI influences. By using Android's `ViewModel`class we can take advantage of all the framework integrations made around it (many of the new APIs are evolving towards `LifecycleOwner` so I think is worth going in the same direction). Also by using MVI concepts like a single class representing View state and driving user actions as unified Intents makes the data flow so much clearer and provides a simpler app state management.

Depending of the app I change some aspects of how VM exposed types are grouped or modeled but at least I always include:  
* `ViewState` class that represents the state of the screen
* `ViewAction` class that represents one shot actions (like showing a Snackbar or navigating to another screen)
* `Intent` class that represents the actions that the user send to the VM

If the app for example has a heavy navigation I tend to split between ViewActions and NavigationActions, but it is just a conceptual grouping.

### DI

DI is done through Koin. Although is not a real DI framework (but a very well designed dependency container) it does the work really well for small apps and its faster that setting up Dagger.  
Also build times are something to keep in mind and in the last releases they also improved how definitions are resolved and indexed so in my opinion every day they get closer to being a real alternative to Dagger.

### UI

Althoug UI implementation is very straightforward (all the presentation work load is done by VM), there is one thing I believe is worth to mention:
All the apps must perform error handling in one or another way and it commonly means giving the user visual feedback about errors that occurs.
Its also very common that this visual error feedback is presented in an unified way (Generic error dialogs, Snackbars, etc) and many times this involves copy pasting the same code in every `Activity` or `Fragment` to handle it.  
To avoid this I came up with the concept of `ErrorDisplay` class that is meant to be implemented by UI components that wants to provide a common way iof presenting errors.

In this case `BaseActivity` implements it and subclasses just need to call when an error is received it and forget about detail implementations of how error displaying is handled.

### Testing

I always do testing (Unit and instrumented) by providing test fakes or mocks for repository or use cases assuming that most of the logic work load is done by the ViewModel.  Also by ensuring fakes in UI testing we can get rid of `IdlingResource`s by making all the data flow sync.
Although I played around with some testing frameworks like Spek I feel more confortable going with plain assertions, only using `Mockito` to mock use cases responses. 

**Note**: Tried to implement [Shot](https://github.com/Karumi/Shot) but it has some issues with new `ActivityScenario` API so I finally did not include it in the final release, although a very simple first approach integration van be found in a separate [branch](https://github.com/imablanco/FancyStore/tree/feature/shot).

## Domain business rules

One of the most important parts of the app is how discounts are computed. (Although in a real app this discounts should be calculated in back).  

Discounts are modeled trough `Discount` sealed class that currently has two inheritors (one by type of known discount)  
* FreeItemDiscount
* BulkDiscount

`FreeItemDiscount` represents MxN discounts where buying M products the user gets M-N for free.
`BulkDiscount` represents X% off discounts where buying N or more products of a type applies X% off on all of them.

To provide an scalable way of creating and applying different discounts I constructed some classes that work together:

### DiscountValidator

It is generic on `Discount` and has the responsability of validating if the given `Discount` type is valid for the received products.

Currently the implementation for known discount types is the same as both works over the same concept of applying over a minimun product count.

### DiscountTransformer

It is also generic on `Discount` and has the responsability of transforming the received list of products for the given `Discount`.

Is designed to work in conjunction with `DiscountValidator` and only perform transformation logic if the `Discount` is valid.

There is a base class called `BaseDiscountTransformer` that takes this into account an provides common logic for validating the `Discount` before applying any transformation.

### DiscountTransformers

This is the main class exposed to perform discount transformations and works by orchestrating all the registered `DiscountTransformers`. Receives a list of products and a list of available discounts and returns the transformerd list of products along the applied discounts.

To resume this process, all this classes works in conjuction in the next way:

1. `DiscountTransformers` `applyDiscounts` method is called
2. For every different `Discount` its associated `DiscountTransformer` `applyDiscount` method is called
3. The `DiscountTransformer` validates if the given `Discount` is valid for the received list of products and applies the required transformations.
4. After all `Discount`s has been processed, the caller receives a new mutated list of products and the applied discounts.


