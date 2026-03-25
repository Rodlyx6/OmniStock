<template>
  <transition name="toast-fade">
    <div v-if="visible" class="toast" :class="type === 'error' ? 'is-error' : 'is-success'">
      {{ message }}
    </div>
  </transition>
</template>

<script setup>
defineProps({
  visible: Boolean,
  message: String,
  type: String
})
</script>
