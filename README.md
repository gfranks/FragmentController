FragmentController
===========

This is an Fragment controller for handling toggling between fragments on a given activity/screen using the LocalBroadcastManager. FragmentController is only available for API levels 15+.

How To Use It:
-------------

### Basic Example

```java
public class MainFragment extends FragmentController {

    private List<String> mTags;

    // return your list of tags defining each possible fragment being displayed
    @NonNull
    @Override
    public List<String> getTagsForFragments() {
        if (mTags == null) {
            mTags = new ArrayList<>();
            mTags.add(FirstFragment.TAG);
            mTags.add(SecondFragment.TAG);
        }
        return mTags;
    }

    // create your arguments, if any, when first creating the initial fragment
    @Override
    public Bundle getInitialArgumentsForFragment(String tag) {
        return new Bundle();
    }

    // initialize the fragment yourself, in case any special constructor/method should be called
    @NonNull
    @Override
    public Fragment createFragmentFromTag(String tag, Bundle args) {
        Fragment fragment;
        if (tag.equals(FirstFragment.TAG)) {
            fragment = new FirstFragment();
        } else {
            fragment = new SecondFragment();
        }

        fragment.setArguments(args);

        return fragment;
    }
}
```

### Toggling between Fragments

```java
public class FirstFragment extends Fragment {

    ...

    // will toggle to the next fragment by referencing the next possble fragment tag
    private void toggle() {
        // you can pass any extras here and they will be passed on to the next fragment
        Intent intent = new Intent(FragmentController.BROADCAST_TOGGLE);
        // intent.putExtra(<name>, <extra>);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    // you may override this as the container fragment will store these values and pass them back in the arguments
    // when resuming, you may check for these values using the getArguments() method
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}

```


Please see the sample project included in this repo for an example.

Installation:
------------

### Directly include source into your projects

- Simply copy the source/resource files from the library folder into your project.

### Use binary approach

- Follow these steps to include aar binary in your project:

    1: Copy com.github.gfranks.fragment.controller-1.0.aar into your projects libs/ directory.

    2: Include the following either in your top level build.gradle file or your module specific one:
    ```
      repositories {
         flatDir {
             dirs 'libs'
         }
     }
    ```
    3: Under your dependencies for your main module's build.gradle file, you can reference that aar file like so:
    ```compile 'com.github.gfranks.fragment.controller:com.github.gfranks.fragment.controller-1.0@aar'```

License
-------
Copyright (c) 2015 Garrett Franks. All rights reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.