// Copyright 2017 The Bazel Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.google.devtools.build.lib.rules.android;

import com.google.devtools.build.lib.actions.Artifact;
import com.google.devtools.build.lib.concurrent.ThreadSafety.Immutable;
import com.google.devtools.build.lib.packages.BuiltinProvider;
import com.google.devtools.build.lib.packages.NativeInfo;
import com.google.devtools.build.lib.skylarkbuildapi.android.AndroidInstrumentationInfoApi;
import com.google.devtools.build.lib.syntax.EvalException;
import com.google.devtools.build.lib.syntax.Runtime;

/**
 * A provider for targets that create Android instrumentations. Consumed by Android testing rules.
 */
@Immutable
public class AndroidInstrumentationInfo extends NativeInfo
    implements AndroidInstrumentationInfoApi<Artifact, ApkInfo> {

  private static final String SKYLARK_NAME = "AndroidInstrumentationInfo";

  public static final AndroidInstrumentationInfoProvider PROVIDER =
      new AndroidInstrumentationInfoProvider();

  private final Artifact targetApk;
  private final Artifact instrumentationApk;
  private final ApkInfo target;

  AndroidInstrumentationInfo(Artifact targetApk, Artifact instrumentationApk) {
    super(PROVIDER);
    this.targetApk = targetApk;
    this.instrumentationApk = instrumentationApk;
    this.target = null;
  }

  AndroidInstrumentationInfo(Artifact targetApk, Artifact instrumentationApk, ApkInfo target) {
    super(PROVIDER);
    this.targetApk = targetApk;
    this.instrumentationApk = instrumentationApk;
    this.target = target;
  }

  @Override
  public Artifact getTargetApk() {
    return targetApk;
  }

  @Override
  public Artifact getInstrumentationApk() {
    return instrumentationApk;
  }

  @Override
  public ApkInfo getTarget() {
    return target;
  }

  /** Provider for {@link AndroidInstrumentationInfo}. */
  public static class AndroidInstrumentationInfoProvider
      extends BuiltinProvider<AndroidInstrumentationInfo>
      implements AndroidInstrumentationInfoApiProvider<Artifact, ApkInfo> {

    private AndroidInstrumentationInfoProvider() {
      super(SKYLARK_NAME, AndroidInstrumentationInfo.class);
    }

    @Override
    public AndroidInstrumentationInfoApi<Artifact, ApkInfo> createInfo(
        Artifact targetApk, Artifact instrumentationApk, Object target) throws EvalException {
      return new AndroidInstrumentationInfo(
          targetApk, instrumentationApk, target == Runtime.NONE ? null : (ApkInfo) target);
    }
  }
}
