/*
 * Copyright 2000-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jetbrains.python;

import com.google.common.collect.Lists;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootModel;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.roots.impl.DirectoryIndexExcludePolicy;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.pointers.VirtualFilePointer;
import com.jetbrains.python.sdk.PythonSdkType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author traff
 */
public class PyDirectoryIndexExcludePolicy implements DirectoryIndexExcludePolicy {

  private final Project myProject;

  public PyDirectoryIndexExcludePolicy(@NotNull Project project) {
    myProject = project;
  }

  @NotNull
  @Override
  public VirtualFile[] getExcludeRootsForProject() {
    List<VirtualFile> result = Lists.newArrayList();
    for (VirtualFile root : ProjectRootManager.getInstance(myProject).getContentRoots()) {
      VirtualFile file = root.findChild(".tox");
      if (file != null) {
        result.add(file);
      }
    }

    for (Module m : ModuleManager.getInstance(myProject).getModules()) {
      Sdk sdk = PythonSdkType.findPythonSdk(m);
      if (sdk != null) {
        for (VirtualFile dir : sdk.getRootProvider().getFiles(OrderRootType.CLASSES)) {
          VirtualFile sitePackages = dir.findChild("site-packages");
          if (sitePackages != null) {
            result.add(sitePackages);
          }
        }
      }
    }

    return result.toArray(new VirtualFile[result.size()]);
  }

  @NotNull
  @Override
  public VirtualFilePointer[] getExcludeRootsForModule(@NotNull ModuleRootModel rootModel) {
    
    
    return VirtualFilePointer.EMPTY_ARRAY;
  }
}
