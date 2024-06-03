package com.example.therecipeapp.models

data class AnalyzedInstructionModel(
    var name: String?,
    var steps: List<StepModel> = emptyList()
)