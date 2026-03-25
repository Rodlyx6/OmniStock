<template>
  <div class="smart-suggest" ref="rootRef">
    <span class="smart-icon">⌕</span>
    <input
      class="input smart-input"
      :placeholder="placeholder"
      v-model="keyword"
      @focus="openPanel"
      @input="onInput"
      @keydown.enter.prevent="selectFirst"
      @keydown.arrow-down.prevent="move(1)"
      @keydown.arrow-up.prevent="move(-1)"
    />
    <button v-if="modelValue" class="smart-clear" @click="clearSelection">×</button>

    <div v-if="open" class="smart-panel">
      <button
        v-for="(option, idx) in filteredOptions"
        :key="option.id"
        class="smart-item"
        :class="{ active: idx === activeIndex }"
        @mouseenter="activeIndex = idx"
        @mousedown.prevent="selectOption(option)"
      >
        <span class="smart-label">{{ option.label }}</span>
        <span v-if="option.meta" class="smart-meta">{{ option.meta }}</span>
      </button>
      <div v-if="filteredOptions.length === 0" class="smart-empty">{{ emptyText }}</div>
    </div>
  </div>
</template>

<script setup>
import { computed, onBeforeUnmount, onMounted, ref, watch } from 'vue'

const props = defineProps({
  modelValue: { type: String, default: '' },
  options: { type: Array, default: () => [] },
  placeholder: { type: String, default: '请输入关键词' },
  emptyText: { type: String, default: '无匹配项' }
})

const emit = defineEmits(['update:modelValue'])

const rootRef = ref(null)
const open = ref(false)
const keyword = ref('')
const activeIndex = ref(0)

const normalize = (text) => (text || '').toLowerCase().replace(/[\s\-_—–]/g, '')

const tokenize = (text) => normalize(text).split(/[^a-z0-9\u4e00-\u9fa5]+/).filter(Boolean)

const scoreOption = (option, inputRaw) => {
  const input = normalize(inputRaw)
  if (!input) return 1

  const label = normalize(option.label)
  const meta = normalize(option.meta)
  const pool = `${label} ${meta}`
  const tokens = tokenize(inputRaw)

  const allTokenMatched = tokens.every((t) => pool.includes(t))
  if (!allTokenMatched) return 0

  if (label.startsWith(input)) return 120 + input.length
  if (label.includes(input)) return 100 + input.length
  if (meta.includes(input)) return 80 + input.length

  return 60 + tokens.join('').length
}

const selectedOption = computed(() => props.options.find((item) => item.id === props.modelValue))

const filteredOptions = computed(() => {
  const rows = props.options
    .map((option) => ({ option, score: scoreOption(option, keyword.value) }))
    .filter((row) => row.score > 0)
    .sort((a, b) => b.score - a.score)
    .map((row) => row.option)
  return rows.slice(0, 8)
})

const syncKeyword = () => {
  keyword.value = selectedOption.value?.label || ''
}

watch(() => props.modelValue, syncKeyword, { immediate: true })
watch(filteredOptions, () => {
  activeIndex.value = 0
})

const openPanel = () => {
  open.value = true
}

const onInput = () => {
  emit('update:modelValue', '')
  open.value = true
}

const selectOption = (option) => {
  emit('update:modelValue', option.id)
  keyword.value = option.label
  open.value = false
}

const move = (step) => {
  if (!open.value) {
    open.value = true
    return
  }
  if (filteredOptions.value.length === 0) return
  const total = filteredOptions.value.length
  activeIndex.value = (activeIndex.value + step + total) % total
}

const selectFirst = () => {
  const option = filteredOptions.value[activeIndex.value] || filteredOptions.value[0]
  if (option) selectOption(option)
}

const clearSelection = () => {
  emit('update:modelValue', '')
  keyword.value = ''
  open.value = true
}

const handleDocClick = (event) => {
  if (!rootRef.value?.contains(event.target)) {
    open.value = false
    syncKeyword()
  }
}

onMounted(() => document.addEventListener('click', handleDocClick))
onBeforeUnmount(() => document.removeEventListener('click', handleDocClick))
</script>
