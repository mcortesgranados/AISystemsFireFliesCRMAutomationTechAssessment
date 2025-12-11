import { FormEvent, useState } from 'react'
import './App.css'

const defaultTranscript = `Meeting Date: November 15, 2024
Participants: Lisa, Jennifer (Real Estate Agent)
Lisa: Hi Jennifer, thanks for hopping on this call. I wanted to walk through where we are with your Follow Up Boss setup and make sure we're on track for your go-live date.
Jennifer: Absolutely! I'm excited to finally get this rolling. I've been so overwhelmed trying to manage my leads manually.
Lisa: Totally understandable. So, based on what we discussed last week, here's what we need to knock out over the next two weeks.
Lisa: First, we need to finalize your lead source tracking. I'll need you to send me a list of all your lead sources - Zillow, Realtor.com, your website, referrals, open houses, etc. - by Friday.
Jennifer Martinez: Got it. I'll pull that together and email it to you by end of week.
Lisa: Perfect. Second, we need to set up your automated drip campaigns. I'm going to need you to review the email templates I sent you last Monday and let me know if you want to make any changes. Can you get me your feedback by Wednesday?
Jennifer Martinez: Yes, I'll review those tonight and send you my edits by Wednesday morning.
Lisa: Awesome. Third, we need to schedule a training session for your assistant, Sarah, so she knows how to use Follow Up Boss for lead entry and task management. Can you have her pick a time on my calendar for next week?
Jennifer Martinez: Absolutely. I'll have her book something today.
Lisa: Great. And lastly, I want to make sure we're integrating your showing software - ShowingTime, right? - with Follow Up Boss so that when a showing gets scheduled, it automatically creates a follow-up task. I'll handle the technical setup, but I'll need your ShowingTime login credentials. Can you send those to me via our secure portal by Thursday?
Jennifer Martinez: Yep, I'll do that tomorrow.
Lisa: Perfect. So just to recap: lead source list by Friday, email template feedback by Wednesday, Sarah books her training for next week, and ShowingTime credentials by Thursday. Does that all sound doable?
Jennifer Martinez: Yes, totally doable. I really appreciate you breaking this down for me.
Lisa: Of course! That's what we're here for. I'll check in with you on Friday to make sure everything's on track, and we should be good to go live by December 1st.
Jennifer Martinez: Sounds great. Thanks, Lisa!
Lisa: Anytime. Talk soon!`

type ActionItem = {
  description?: string
  priority?: string
  assignee?: string
  deadline?: string
}

type TaskResult = {
  hubspotTaskId?: string
  description?: string
  hubspotTaskUrl?: string
}

function App() {
  const [transcript, setTranscript] = useState(defaultTranscript)
  const [response, setResponse] = useState<Record<string, unknown> | null>(null)
  const [deleteResponse, setDeleteResponse] = useState<Record<string, unknown> | null>(null)
  const [loading, setLoading] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')
  const [deleteLoading, setDeleteLoading] = useState(false)
  const [deleteErrorMessage, setDeleteErrorMessage] = useState('')
  const [showDeleteModal, setShowDeleteModal] = useState(false)

  const actionItems = (Array.isArray(response?.actionItems)
    ? response.actionItems
    : []) as ActionItem[]
  const taskResults = (Array.isArray(response?.taskResults)
    ? response.taskResults
    : []) as TaskResult[]
  const deleteStatuses = (Array.isArray(deleteResponse?.statuses)
    ? deleteResponse.statuses
    : []) as {
    dealId?: string
    deleted?: boolean
    message?: string
  }[]
  const statusChipLabel = deleteLoading
    ? 'Deleting…'
    : deleteErrorMessage
    ? 'Delete failed'
    : deleteResponse
    ? `Deleted ${String(
        deleteResponse.totalDeleted ?? '0',
      )} of ${String(deleteResponse.totalFound ?? '0')} deals`
    : errorMessage
    ? 'Error occurred'
    : response
    ? 'Response received'
    : 'Ready to submit'

  const closeDeleteModal = () => setShowDeleteModal(false)

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setErrorMessage('')
    setResponse(null)
    setDeleteResponse(null)
    setLoading(true)

    try {
      const res = await fetch(
        'http://44.198.115.169:9090/api/openai/create-deal-from-transcript-to-hubspot',
        {
          method: 'POST',
          headers: {
            Accept: '*/*',
            'Content-Type': 'text/plain',
          },
          body: transcript,
        },
      )

      if (!res.ok) {
        throw new Error(`${res.status} ${res.statusText}`)
      }

      const payload = await res.json()
      setResponse(payload)
    } catch (error) {
      const message = error instanceof Error ? error.message : 'Unknown error'
      setErrorMessage(message)
    } finally {
      setLoading(false)
    }
  }

  const handleDeleteDeals = async () => {
    setDeleteErrorMessage('')
    setDeleteResponse(null)
    setDeleteLoading(true)

    try {
      const res = await fetch(
        'http://44.198.115.169:9090/api/hubspot/delete-all-deals',
        {
          method: 'DELETE',
          headers: {
            Accept: '*/*',
          },
        },
      )

      if (!res.ok) {
        throw new Error(`${res.status} ${res.statusText}`)
      }

      const payload = await res.json()
      setDeleteResponse(payload)
      setResponse(null)
      setErrorMessage('')
      setShowDeleteModal(true)
    } catch (error) {
      const message = error instanceof Error ? error.message : 'Unknown error'
      setDeleteErrorMessage(message)
      setShowDeleteModal(true)
    } finally {
      setDeleteLoading(false)
    }
  }

  return (
    <>
      <main className="app-shell">
        <header>
          <p className="eyebrow">CRM Automation</p>
          <h1>Create HubSpot deals from transcripts</h1>
          <p className="lede">
            Paste in your meeting notes, post them to the automation endpoint, and preview the HubSpot tasks it spins up.
          </p>
        </header>

        <section className="card form-card">
          <h2>Send a transcript</h2>
          <p className="section-copy">
            The server ingests raw conversation text and returns structured action items and task metadata.
          </p>

          <form className="transcript-form" onSubmit={handleSubmit}>
            <label htmlFor="transcript">Meeting transcript</label>
            <textarea
              id="transcript"
              rows={12}
              value={transcript}
              onChange={(event) => setTranscript(event.target.value)}
            />

            <div className="form-actions">
              <button type="submit" disabled={loading}>
                {loading ? 'Sending…' : 'Run POST'}
              </button>
              <button
                type="button"
                className="secondary-button"
                disabled={!response || deleteLoading}
                onClick={handleDeleteDeals}
              >
                {deleteLoading ? 'Deleting…' : 'Delete all deals'}
              </button>
              <span className="status-chip">{statusChipLabel}</span>
            </div>
            {errorMessage && <p className="error-text">Error: {errorMessage}</p>}
          </form>
        </section>

        {response && (
          <>
            <section className="card response-card">
              <h2>Action items</h2>
              {actionItems.length > 0 ? (
                <div className="item-grid">
                  {actionItems.map((item, index) => (
                    <article className="item-card" key={`${item.description ?? index}-${index}`}>
                      <p className="item-priority">{(item.priority as string) ?? 'Priority: N/A'}</p>
                      <h3>{(item.description as string) ?? 'No description'}</h3>
                      <p className="item-meta">
                        <span>Assignee: {(item.assignee as string) ?? 'Unassigned'}</span>
                        <span>Deadline: {(item.deadline as string) ?? 'None'}</span>
                      </p>
                    </article>
                  ))}
                </div>
              ) : (
                <p className="empty-state">The response did not include action items.</p>
              )}
            </section>

            <section className="card response-card">
              <h2>HubSpot tasks</h2>
              {taskResults.length > 0 ? (
                <div className="task-list">
                  {taskResults.map((task, index) => (
                    <article className="task-card" key={`${task.hubspotTaskId ?? index}-${index}`}>
                      <div>
                        <p className="item-priority">Task ID: {(task.hubspotTaskId as string) ?? 'N/A'}</p>
                        <h3>{(task.description as string) ?? 'No description'}</h3>
                      </div>
                      {(task.hubspotTaskUrl as string) && (
                        <a
                          href={task.hubspotTaskUrl as string}
                          target="_blank"
                          rel="noreferrer"
                          className="task-link"
                        >
                          Open in HubSpot
                        </a>
                      )}
                    </article>
                  ))}
                </div>
              ) : (
                <p className="empty-state">No HubSpot tasks were returned.</p>
              )}
            </section>

            <section className="card response-card">
              <h2>Raw JSON response</h2>
              <pre className="json-block">{JSON.stringify(response, null, 2)}</pre>
            </section>
          </>
        )}
      </main>
      {showDeleteModal && (
        <div
          className="modal-overlay"
          role="dialog"
          aria-modal="true"
          onClick={closeDeleteModal}
        >
          <div className="modal" onClick={(event) => event.stopPropagation()}>
            <button
              type="button"
              className="modal-close"
              onClick={closeDeleteModal}
              aria-label="Close delete results"
            >
              ×
            </button>
            <h2>Delete all deals</h2>
            {deleteErrorMessage ? (
              <p className="error-text">Error: {deleteErrorMessage}</p>
            ) : deleteResponse ? (
              <>
                <p className="section-copy">
                  Deleted{' '}
                  {String(
                    (deleteResponse.totalDeleted as number | string | undefined) ??
                      'N/A',
                  )}{' '}
                  of{' '}
                  {String(
                    (deleteResponse.totalFound as number | string | undefined) ??
                      'N/A',
                  )}{' '}
                  deals.
                </p>
                {deleteStatuses.length > 0 ? (
                  <div className="delete-statuses">
                    {deleteStatuses.map((status, index) => (
                      <article
                        className="delete-status-card"
                        key={`${status.dealId ?? 'deal'}-${index}`}
                      >
                        <p className="item-priority">
                          {status.dealId ?? 'Deal ID missing'}
                        </p>
                        <p>{status.message ?? 'No status message'}</p>
                        <p className="item-meta">
                          Status:{' '}
                          <span>
                            {status.deleted ? 'Deleted ✓' : 'Not deleted'}
                          </span>
                        </p>
                      </article>
                    ))}
                  </div>
                ) : (
                  <p className="empty-state">No statuses were returned.</p>
                )}
                <pre className="json-block">
                  {JSON.stringify(deleteResponse, null, 2)}
                </pre>
              </>
            ) : (
              <p className="section-copy">Waiting for delete response...</p>
            )}
            <button
              type="button"
              className="modal-close-button"
              onClick={closeDeleteModal}
            >
              Close
            </button>
          </div>
        </div>
      )}
    </>
  )
}

export default App
