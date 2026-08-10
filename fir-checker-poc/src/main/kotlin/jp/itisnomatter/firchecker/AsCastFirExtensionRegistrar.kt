package jp.itisnomatter.firchecker

import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class AsCastFirExtensionRegistrar : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::AsCastCheckersExtension
    }
}
