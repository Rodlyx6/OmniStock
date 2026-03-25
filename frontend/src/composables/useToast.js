import { ref } from 'vue'

const visible = ref(false)
const message = ref('')
const type = ref('success')
let timer = null

export function useToast() {
  const show = (msg, toastType = 'success') => {
    message.value = msg
    type.value = toastType
    visible.value = true
    if (timer) {
      clearTimeout(timer)
    }
    timer = setTimeout(() => {
      visible.value = false
    }, 2200)
  }

  return {
    visible,
    message,
    type,
    success: (msg) => show(msg, 'success'),
    error: (msg) => show(msg, 'error')
  }
}
