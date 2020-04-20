/*
 * Copyright 2020 moznion.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.moznion.sbt.spotless.config

import java.io.File

import net.moznion.sbt.spotless.Target
import net.moznion.sbt.spotless.Target.{IsFile, IsString}

object SpotlessConfig {
  private val dependencyWorkingDir = "target"
  private val dynamicDependencyCacheDir = ".spotlessDepCache"
}

case class SpotlessConfig(
    baseDir: Target = null,
    dynamicDependencyWorkingDir: Target = null,
    dynamicDependencyCacheDir: Target = null,
    disableDynamicDependencyCache: Boolean = false,
    disableDynamicDependencyResolving: Boolean = false,
) {
  private[sbt] def toPathConfig(defaultBaseDir: File): SpotlessPathConfig = {
    val defaultBaseDirTarget: Target = defaultBaseDir
    val baseDir: File = Option(this.baseDir).getOrElse(defaultBaseDirTarget) match {
      case IsString(strPath) => new File(defaultBaseDir, strPath)
      case IsFile(file)      => file
    }

    val defaultDynamicDependencyWorkingDir: Target =
      new File(baseDir, SpotlessConfig.dependencyWorkingDir)
    val dynamicDependencyWorkingDir: File =
      Option(this.dynamicDependencyWorkingDir).getOrElse(defaultDynamicDependencyWorkingDir) match {
        case IsString(strPath) => new File(baseDir, strPath)
        case IsFile(file)      => file
      }

    val defaultDynamicDependencyCacheDir: Target =
      new File(dynamicDependencyWorkingDir, SpotlessConfig.dynamicDependencyCacheDir)
    val dynamicDependencyCacheDir: File =
      Option(this.dynamicDependencyCacheDir).getOrElse(defaultDynamicDependencyCacheDir) match {
        case IsString(strPath) => new File(baseDir, strPath)
        case IsFile(file)      => file
      }

    SpotlessPathConfig(
      baseDir = baseDir,
      dynamicDependencyWorkingDir = dynamicDependencyWorkingDir,
      dynamicDependencyCacheDir = dynamicDependencyCacheDir,
    )
  }
}
